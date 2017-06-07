package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class partidaAdapter extends RecyclerView.Adapter<partidaAdapter.partidaViewHolder> implements itemClickListener{
    private final Context contexto;
    private CharSequence palabra = "CHANCHO";
    private List<String> jugadors;
    public static int cantJugadores = 0;

    public partidaAdapter(List<String> jugadors, Context context) {
        this.jugadors = jugadors;
        this.contexto = context;
        cantJugadores = jugadors.size();
    }

    @Override
    public partidaAdapter.partidaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_partida,parent,false);
        return new partidaAdapter.partidaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(partidaAdapter.partidaViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position));
        int numero;
        Random r = new Random();
        numero=(r.nextInt(19)+1);
        String s = "pig"+(numero);
        int id = contexto.getResources().getIdentifier(s,"drawable",contexto.getPackageName());
        holder.foto.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    @Override
    public void onItemClick(final View v, final ImageView foto, final TextView chancho, final TextView chanchoFondo, final TextView nom, final int position) {
        if (chancho.length()<11){
            String s = chancho.getText().toString() + palabra.charAt(chancho.getText().length());
            if (s.length() >= 0) {
                chancho.setText(s.subSequence(0, s.length()));
            }
            if (chancho.getText().length() == 7) {
                chancho.setText("¡CHANCHO VA!");
                chanchoFondo.setText("");
                foto.setAlpha(0.5f);
                Toast.makeText(v.getContext(), nom.getText()+" perdio.", Toast.LENGTH_SHORT).show();
                jugadors.remove(position);
                cantJugadores -= 1;
                if (cantJugadores == 1){
                    String jugador = jugadors.get(0);
                    Toast.makeText(v.getContext(), "ganó "+jugador, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    public static class partidaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private CharSequence palabra = "CHANCHO";
        public ImageView foto;
        public TextView nom;
        public TextView chanchoFondo;
        public ImageView img;
        public TextView chancho;
        public itemClickListener listener;
        public partidaViewHolder(View v, itemClickListener listener){
            super(v);
            foto= (ImageView)v.findViewById(R.id.fotocarnet);
            chanchoFondo = (TextView)v.findViewById(R.id.tvchanchoVacio);
            nom = (TextView) v.findViewById(R.id.tvNombreJugadorPartida);
            chancho = (TextView) v.findViewById(R.id.tvchancho);
            this.listener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            listener.onItemClick(v, foto, chancho, chanchoFondo, nom, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (chancho.length()>0 && chancho.length()<11 ){
                chancho.setText(chancho.getText().toString().substring(0,chancho.getText().toString().length()-1));
            }
            if (chancho.getText().toString().equals("¡CHANCHO VA!")){
                chancho.setText("CHANCH");
                foto.setAlpha(1f);
                chanchoFondo.setText("CHANCHO");
                partidaAdapter.cantJugadores += 1;
            }
            return true;
        }
    }
}

interface itemClickListener{
    void onItemClick(View v, ImageView i, TextView c, TextView cf, TextView n,  int position);
}
