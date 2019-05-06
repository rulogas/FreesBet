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
import com.example.freesbet.AnadirEvento;
import com.example.freesbet.R;

import java.util.ArrayList;

public class CustomAdapterSpinnerCompetidores extends ArrayAdapter {

    Context context;
    ArrayList<String> nombres;

    public CustomAdapterSpinnerCompetidores(Context context, ArrayList<String> nombres) {
        super(context, R.layout.spinner_competidores,nombres);
        this.context = context;
        this.nombres = nombres;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =inflater.inflate(R.layout.spinner_competidores,null);
        TextView t1 = row.findViewById(R.id.textView_spinner_competidor);
        t1.setText(nombres.get(position));
        return row;
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =inflater.inflate(R.layout.spinner_competidores,null);
        TextView t1 = row.findViewById(R.id.textView_spinner_competidor);
        t1.setText(nombres.get(position));
        if ( context instanceof AnadirEvento) {
            t1.setTextColor(Color.WHITE);
        }
        return row;
    }
}
