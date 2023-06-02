package com.example.caminoalba;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caminoalba.models.Badge;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.ui.menuItems.partner.FragmentPartner;
import com.example.caminoalba.ui.menuItems.partner.RecyclerviewPartner;
import com.example.caminoalba.ui.menuItems.publication.FragmentMap;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.barcode.BarcodeDetector.Builder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentConfirmationPartnership extends Fragment {

    private DatabaseReference mDatabase;
    private SurfaceView cameraPreview;
    private boolean isConfirmationShown = false;
    private Profile profile;
    private ConfirmationCallback callback;

    public FragmentConfirmationPartnership() {
    }

    public interface ConfirmationCallback {
        void onBadgeAdded(Badge badge);

        void onConfirmationComplete(Profile updatedProfile);
    }

    public void setCallback(ConfirmationCallback callback) {
        this.callback = callback;
    }

    // Call this method when a badge is added
    void onBadgeAdded(Badge badge) {
        if (callback != null) {
            callback.onBadgeAdded(badge);
        }
    }

    // Call this method when the confirmation process is complete
    void onConfirmationComplete(Profile updatedProfile) {
        if (callback != null) {
            callback.onConfirmationComplete(updatedProfile);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation_partnership, container, false);
        cameraPreview = view.findViewById(R.id.camera_preview);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Gson gson = new Gson();
        String profileStr = preferences.getString("profile", "");
        profile = gson.fromJson(profileStr, Profile.class);

        // Initialize the mDatabase variable
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void startCamera() {
        BarcodeDetector barcodeDetector = new Builder(requireContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        CameraSource cameraSource = new CameraSource.Builder(requireContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Handle camera permission not granted
                        Toast.makeText(requireContext(), R.string.camera_permisson_no_granted, Toast.LENGTH_SHORT).show();
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
            public void release() {
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() > 0 && !isConfirmationShown) {
                    requireActivity().runOnUiThread(() -> {
                        String qrCodeText = qrCodes.valueAt(0).displayValue;
                        isConfirmationShown = true;
                        if (!qrCodeText.equalsIgnoreCase(profile.getCurrentPath())) {
                            Toast.makeText(requireContext(), R.string.qr_dont_belong_here, Toast.LENGTH_SHORT).show();
                        } else if (!FragmentMap.isEnabled) {
                            Toast.makeText(requireContext(), R.string.beSureToBeLess200mt, Toast.LENGTH_SHORT).show();
                        } else {
                            search(qrCodeText);
                        }
                    });
                }
            }
        });
    }

    private void updateParentFragment(Badge badge) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FragmentPartner) {
            FragmentPartner fragmentPartner = (FragmentPartner) parentFragment;
            fragmentPartner.onBadgeAdded(badge);
        }
    }


    private void search(String query) {
        Query searchQuery = mDatabase.child("profiles").orderByChild("profile_id").equalTo(profile.getProfile_id());
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    .setTitle(R.string.confirmation)
                                    .setMessage(R.string.wanna_give_a_badge)
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        // Update the profile object in the database
                                        Map<String, Object> updateData = new HashMap<>();

                                        // Get the existing list of badges
                                        List<Badge> badges = profile.getBadges();
                                        // If badges list is null, create a new ArrayList to store the badges
                                        if (badges == null) {
                                            badges = new ArrayList<>();
                                        }

                                        String newBadgeName = profile.getCurrentPath();
                                        boolean badgeAlreadyExists = false;

                                        // Check if a badge with the same name already exists
                                        for (Badge existingBadge : badges) {
                                            if (existingBadge.getName().equalsIgnoreCase(newBadgeName)) {
                                                badgeAlreadyExists = true;
                                                break;
                                            }
                                        }

                                        if (badgeAlreadyExists) {
                                            // A badge with the same name already exists
                                            Toast.makeText(requireContext(), R.string.user_already_has_this_path, Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Add the new badge to the list
                                            LocalDateTime datePublished = LocalDateTime.now();
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                                            String formattedDate = datePublished.format(formatter);

                                            Badge badge = new Badge(newBadgeName, formattedDate);
                                            badges.add(badge);
                                            updateData.put("badges", badges);

                                            // Update the profile in the database
                                            DatabaseReference profileRef = profileSnapshot.getRef();
                                            profileRef.updateChildren(updateData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Call the onBadgeAdded method to notify the callback
                                                        onBadgeAdded(badge);

                                                        // Update the parent fragment (if applicable)
                                                        updateParentFragment(badge);

                                                        // Show a toast message
                                                        Toast.makeText(requireContext(), R.string.badge_added_successfully, Toast.LENGTH_SHORT).show();

                                                        // Call the onConfirmationComplete method to notify the callback
                                                        onConfirmationComplete(profile);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Handle the database update failure
                                                        Toast.makeText(requireContext(), R.string.database_error, Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    })
                                    .setNegativeButton("No", (dialog, which) -> {
                                        // Call the onConfirmationComplete method with the current profile to notify the callback
                                        onConfirmationComplete(profile);
                                    })
                                    .setOnDismissListener(dialog -> {
                                        isConfirmationShown = false;
                                    })
                                    .show();
                        } else {
                            // Current path is null, show error message
                            Toast.makeText(requireContext(), R.string.error_current_path_null, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Profile ID not found
                    Toast.makeText(requireContext(), R.string.profile_not_found, Toast.LENGTH_SHORT).show();
                    isConfirmationShown = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(requireContext(), R.string.database_error, Toast.LENGTH_SHORT).show();
                isConfirmationShown = false;
            }
        });
    }


    private void stopCamera() {
        cameraPreview.getHolder().removeCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }
}
