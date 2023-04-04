package com.example.caminoalba.ui.menuItems.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.R;
import com.example.caminoalba.models.User;

public class ConfirmEmailFragment extends Fragment {

    private EditText etConfirmCode;
    private Button btnConfirmCode;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etConfirmCode = view.findViewById(R.id.edInsertCodeNumber);
        btnConfirmCode = view.findViewById(R.id.btnConfirmCode);
        //Retrieve the args that we've passed from our parent fragment
        assert getArguments() != null;
        user = (User) getArguments().getSerializable("user");
        confirmationPassword();
    }


//    public void updateUser(User user) {
//        Call<Boolean> call = iapIservice.updateUser(user);
//        System.out.println("esto es user desde childfragment " + user);
//        call.enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                Toast.makeText(getActivity(), "Email verified successfully !!! ", Toast.LENGTH_SHORT).show();
//                // In the child fragment, get a reference to the FragmentManager
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                // Pop the back stack to go back to the parent fragment
//                fragmentManager.popBackStack();
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Toast.makeText(getContext(), "Update unsuccessfully", Toast.LENGTH_SHORT).show();
//                // In the child fragment, get a reference to the FragmentManager
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                // Pop the back stack to go back to the parent fragment
//                fragmentManager.popBackStack();
//            }
//        });
//    }


    public void confirmationPassword() {
        btnConfirmCode.setOnClickListener(v -> {
            if (etConfirmCode.getText().toString().equalsIgnoreCase(user.getVerificationCode())) {
                user.setEnabled(true);
                System.out.println("esto es user desde method  " + user);
//                updateUser(user);
            } else {
                Toast.makeText(getActivity(), "Sorry, the email hasn't beend verified successfully !!! ", Toast.LENGTH_SHORT).show();
            }
        });

    }


}