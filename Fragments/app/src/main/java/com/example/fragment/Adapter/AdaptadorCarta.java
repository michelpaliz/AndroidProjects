package com.example.fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fragment.Model.Letter;
import com.example.fragment.R;

import java.util.List;

public class AdaptadorCarta extends ArrayAdapter<Letter> {
    private Context context;
    private List<Letter>letters;

    public AdaptadorCarta(Fragment context, List<Letter> letters) {
        super(context.requireActivity(), R.layout.listitem_correo,letters);
        this.letters = letters;
        this.context = context.getActivity();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
         LayoutInflater inflater = LayoutInflater.from(context);
         convertView = inflater.inflate(R.layout.listitem_correo,null);
         TextView tvDetalle = convertView.findViewById(R.id.tvDetalle);
         tvDetalle.setText(letters.get(position).getDato());
         TextView tvAsunto = convertView.findViewById(R.id.tvAsunto);
         tvAsunto.setText(letters.get(position).getDetalle());
         return convertView;
     }


}
