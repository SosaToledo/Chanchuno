package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import java.util.Set;


public class partidaAdapter extends RecyclerView.Adapter<partidaAdapter.partidaViewHolder>{
    private final Context contexto;
    private List numeros = new ArrayList<Integer>();
    CharSequence palabra = "CHANCHO";
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
        return new partidaAdapter.partidaViewHolder(v);
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


    public static class partidaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        CharSequence palabra = "CHANCHO";
        public ImageView foto;
        public TextView nom;
        public TextView chanchoFondo;
        public ImageView img;
        public TextView chancho;
        public partidaViewHolder(View v){
            super(v);
            foto= (ImageView)v.findViewById(R.id.fotocarnet);
            chanchoFondo = (TextView)v.findViewById(R.id.tvchanchoVacio);
            nom = (TextView) v.findViewById(R.id.tvNombreJugadorPartida);
            chancho = (TextView) v.findViewById(R.id.tvchancho);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (chancho.length()<11){
                String s = chancho.getText().toString() + palabra.charAt(chancho.getText().length());
                if (s.length() >= 0) {
                    chancho.setText(s.subSequence(0, s.length()));
                }
                if (chancho.getText().length() == 7) {
                    AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(v.getContext());
                    dialogEliminar.setTitle("¿"+nom.getText()+" perdió?");
                    dialogEliminar.setCancelable(false);
                    dialogEliminar.setPositiveButton("sí", new DialogInterface.OnClickListener() {

                        //caso afirmativo el jugador es eliminado.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chancho.setText("¡CHANCHO VA!");
                            chanchoFondo.setText("");
                            foto.setAlpha(0.5f);
                            Toast.makeText(v.getContext(), nom.getText()+" perdio.", Toast.LENGTH_SHORT).show();
                            partida.eliminar(nom.getText().toString());
                            partidaAdapter.cantJugadores -= 1;
                            if (partidaAdapter.cantJugadores == 1){
                                String s = MainActivity.jugadors.get(0);
                                Toast.makeText(v.getContext(), "ganó "+s, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogEliminar.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        //caso negativo el juegador vuelve a quedar a una letra de perder.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chancho.setText("CHANCH");
                            foto.setAlpha(1f);
                            chanchoFondo.setText("CHANCHO");
                        }
                    });
                    dialogEliminar.show();

                }
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (chancho.length()>0 && chancho.length()<11 ){
                chancho.setText(chancho.getText().toString().substring(0,chancho.getText().toString().length()-1));
            }
            if (chancho.getText().toString().equals(new String("¡CHANCHO VA!"))){
                chancho.setText("CHANCH");
                foto.setAlpha(1f);
                chanchoFondo.setText("CHANCHO");
                partidaAdapter.cantJugadores += 1;
            }
            return true;
        }
    }
}
