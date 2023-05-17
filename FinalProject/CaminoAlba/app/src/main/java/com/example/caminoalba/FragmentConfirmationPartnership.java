package com.example.caminoalba;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.models.Profile;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class FragmentConfirmationPartnership extends Fragment {

    private DatabaseReference mDatabase;
    private TextInputEditText mSearchEditText;
    private Button scanButton;
    private SurfaceView cameraPreview;
    private boolean isConfirmationShown = false;

    public FragmentConfirmationPartnership() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation_partnership, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        cameraPreview = view.findViewById(R.id.camera_preview);
        cameraAction();

        return view;
    }

    public void cameraAction() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(requireContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        CameraSource cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector)
                .setRequestedPreviewSize(150, 150)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Handle camera permission not granted
                        return;
                    }

                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() > 0 && !isConfirmationShown) {
                    String qrCodeText = qrCodes.valueAt(0).displayValue;
                    isConfirmationShown = true;
                    // Process the scanned QR code here
                    search(qrCodeText);
                }
            }
        });
    }

    private void search(String query) {
        Query searchQuery = mDatabase.child("profiles").orderByChild("profile_id").equalTo(query);
        (searchQuery).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Profile ID found
                    for (DataSnapshot profileSnapshot : snapshot.getChildren()) {
                        Profile profile = profileSnapshot.getValue(Profile.class);
                        assert profile != null;
                        if (profile.getCurrentPath() != null) {
                            // Prompt the user for confirmation
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you want to give this profile a badge for this path?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        // User clicked yes, update the path
                                        profile.addPath(profile.getCurrentPath());
                                        profileSnapshot.getRef().setValue(profile);
                                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                        isConfirmationShown = false; // Reset the flag
                                    })
                                    .setNegativeButton("No", (dialog, which) -> {
                                        isConfirmationShown = false; // Reset the flag
                                    })
                                    .show();
                        } else {
                            Toast.makeText(requireContext(), "The user needs to be in a placemark in order to assign the user the badge", Toast.LENGTH_SHORT).show();
                            isConfirmationShown = false; // Reset the flag
                        }
                    }
                } else {
                    // Profile ID not found
                    // Profile object not found
                    Toast.makeText(requireContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                    isConfirmationShown = false; // Reset the flag
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isConfirmationShown = false; // Reset the flag
            }
        });
    }
}
