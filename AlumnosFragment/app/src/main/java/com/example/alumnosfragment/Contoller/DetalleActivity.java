package com.example.alumnosfragment.Contoller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.alumnosfragment.Model.Alumno;
import com.example.alumnosfragment.Model.Asignatura;
import com.example.alumnosfragment.R;

import java.util.List;
import java.util.Objects;

public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_NAME="com.example.alumnosfragment.EXTRA_NAME";

    public DetalleActivity() {
        super(R.layout.activity_detalle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
           Alumno alumno = (Alumno) Objects.requireNonNull(getIntent().getSerializableExtra(EXTRA_NAME));
           Bundle bundle = new Bundle();
           bundle.putSerializable(FragmentDetalle.EXTRA_DETALLE,alumno  );
           FragmentManager manager = getSupportFragmentManager();
           manager.beginTransaction().setReorderingAllowed(true).add(R.id.FrgDetalle, FragmentDetalle.class, bundle).commit();

        }


    }

}
