package com.example.caminoalba.ui.menuItems.partner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.caminoalba.R;
import com.example.caminoalba.models.Path;

public class FragmentPathInformation extends Fragment {

    private static final String ARG_PATH = "path";
    private static final String ARG_EXPANDED = "expanded";

    private Path path;
    private boolean isExpanded = true;
    private boolean isAnimating = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getParcelable(ARG_PATH);
            isExpanded = getArguments().getBoolean(ARG_EXPANDED);
        }
    }

    public static FragmentPathInformation newInstance(Path localPath, boolean isExpanded) {
        FragmentPathInformation fragment = new FragmentPathInformation();
        Bundle args = new Bundle();
        args.putSerializable("path", localPath);
        args.putBoolean(ARG_EXPANDED, isExpanded);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_path_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewInfo = view.findViewById(R.id.info_text_view);
        TextView textViewName = view.findViewById(R.id.name_text_view);
        ImageView imageView = view.findViewById(R.id.image_view);


        // Retrieve the path object from the bundle
        Bundle args = getArguments();
        if (args != null) {
            path = (Path) args.getSerializable("path");
            isExpanded = args.getBoolean(ARG_EXPANDED);
        }


        if (path != null) {
            String nombre = "c" + path.getId();
            int id = requireContext().getResources().getIdentifier(nombre, "drawable", requireContext().getPackageName());
            imageView.setImageResource(id);
            textViewName.setText(path.getName());
            textViewInfo.setText(path.getInformation());
        }


        // Initialize your views and set up your button click listener as before

        Button buttonExpandCollapse = view.findViewById(R.id.button_expand_collapse);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        int originalHeight = nestedScrollView.getHeight();

        buttonExpandCollapse.setOnClickListener(v -> {
            if (!isAnimating) {
                isAnimating = true;
                if (isExpanded) {
                    // Collapse the NestedScrollView
                    ValueAnimator anim = ValueAnimator.ofInt(nestedScrollView.getMeasuredHeight(), originalHeight);
                    anim.addUpdateListener(animation -> {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = nestedScrollView.getLayoutParams();
                        layoutParams.height = val;
                        nestedScrollView.setLayoutParams(layoutParams);
                    });
                    anim.setDuration(100);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimating = false;
                        }
                    });
                    anim.start();
                    isExpanded = false;
                } else {
                    // Expand the NestedScrollView
                    nestedScrollView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int targetHeight = nestedScrollView.getMeasuredHeight();
                    nestedScrollView.getLayoutParams().height = originalHeight;
                    nestedScrollView.requestLayout();
                    ValueAnimator anim = ValueAnimator.ofInt(originalHeight, targetHeight);
                    anim.addUpdateListener(animation -> {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = nestedScrollView.getLayoutParams();
                        layoutParams.height = val;
                        nestedScrollView.setLayoutParams(layoutParams);
                    });
                    anim.setDuration(100);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimating = false;
                        }
                    });
                    anim.start();
                    isExpanded = true;
                }
            }
        });
    }


}