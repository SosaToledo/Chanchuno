package com.example.luciano.chanchuno;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PartidaBaseAdapter extends BaseAdapter {

    private static LayoutInflater view = null;

    private String[][] item;
    private Context context;

    PartidaBaseAdapter(String[][]  item, Context context) {
        this.item = item;
        this.context = context;

        view = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint({"ViewHolder", "InflateParams"}) final View vista = view.inflate(R.layout.cv_partida, null);

        ImageView imageView = vista.findViewById(R.id.fotocarnet);
        TextView nombrejugador = vista.findViewById(R.id.tvNombreJugadorPartida);
        TextView chancho = vista.findViewById(R.id.tvchancho);

        nombrejugador.setText(item[position][0]);
        chancho.setText(item[position][1]);
        imageView.setImageResource(R.drawable.pig1);


        return vista;
    }
}
