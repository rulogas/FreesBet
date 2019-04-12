package com.example.freesbet.bases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.R;

import java.util.List;

public class AdapterGridPremios extends BaseAdapter {

    static List<Premio> premios;
    private Context mContext;

    public AdapterGridPremios(Context mContext, List<Premio> premios) {
        this.mContext = mContext;
        this.premios = premios;
    }

    @Override
    public int getCount() {
        return premios.size();
    }

    @Override
    public Premio getItem(int position) {
        return premios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }

        ImageView imagenPremio = (ImageView) convertView.findViewById(R.id.ImageView_imagen_premio);
        TextView nombrePremio = (TextView) convertView.findViewById(R.id.textView_nombrePremio);
        TextView coinsPremio = (TextView) convertView.findViewById(R.id.textView_coinsPremio);

        Glide.with(mContext)
                .load(premios.get(position).getUrlImagen())
                .into(imagenPremio);
        nombrePremio.setText(premios.get(position).getNombre());
        coinsPremio.setText(Integer.toString(premios.get(position).getCosteCoins())+" coins");



        return convertView;
    }


}
