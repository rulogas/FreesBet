package com.example.freesbet.bases;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.R;

import org.w3c.dom.Text;

public class CustomAdpaterSpinner extends ArrayAdapter {
    Context context;
    String[] nombres;
    String[] urlsImagen;

    public CustomAdpaterSpinner(Context context, String[] nombres, String[] urlsImagen) {
        super(context,R.layout.spinner_eventos_paises,nombres);
        this.context = context;
        this.nombres = nombres;
        this.urlsImagen = urlsImagen;
    }

    @Override
    public View getDropDownView(int position,  View convertView, ViewGroup parent) {


        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =inflater.inflate(R.layout.spinner_eventos_paises,null);
        TextView t1 = (TextView)row.findViewById(R.id.textViewPais);
        t1.setTextColor(Color.BLACK);
        ImageView i1 = (ImageView)row.findViewById(R.id.imageViewPais);
        t1.setText(nombres[position]);
        Glide.with(context)
                .load(urlsImagen[position])
                .into(i1);

        return row;
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =inflater.inflate(R.layout.spinner_eventos_paises,null);
        TextView t1 = (TextView)row.findViewById(R.id.textViewPais);
        t1.setTextColor(Color.WHITE);
        ImageView i1 = (ImageView)row.findViewById(R.id.imageViewPais);
        t1.setText(nombres[position]);
        Glide.with(context)
                .load(urlsImagen[position])
                .into(i1);

        return row;
    }
}
