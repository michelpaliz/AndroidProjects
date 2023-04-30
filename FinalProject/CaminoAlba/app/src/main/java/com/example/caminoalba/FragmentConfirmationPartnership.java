package com.example.caminoalba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.models.Profile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentConfirmationPartnership extends Fragment {

    private DatabaseReference mDatabase;
    private TextInputEditText mSearchEditText;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirmation_partnership, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSearchEditText = view.findViewById(R.id.editText_search_user);
        btnSearch = view.findViewById(R.id.button_search_user);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragment();
    }

    public void initFragment() {
        btnSearch.setOnClickListener(v -> mSearchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(textView.getText().toString());
                return true;
            }
            return false;
        }));
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
                            // Do something with the profile object
                            // Prompt the user for confirmation
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure you want to give this profile a badge for this path?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        // User clicked yes, update the path
                                        profile.addPath(profile.getCurrentPath());
                                        profileSnapshot.getRef().setValue(profile);
                                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }else{
                            Toast.makeText(requireContext(), "The user needs to be in a placemark in order to assign the user the badge", Toast.LENGTH_SHORT).show();
                        }

                    }


                } else {
                    // Profile ID not found
                    // Profile object not found
                    Toast.makeText(requireContext(), "Profile not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
