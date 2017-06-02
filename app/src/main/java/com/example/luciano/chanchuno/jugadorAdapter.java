package com.example.luciano.chanchuno;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by luciano on 23/05/17.
 */

public class jugadorAdapter extends RecyclerView.Adapter<jugadorAdapter.jugadorViewHolder> {

    private List<String> jugadors;

    public jugadorAdapter(List<String> jugadors) {
        this.jugadors = jugadors;
    }

    @Override
    public jugadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nombres,parent,false);
        return new jugadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(jugadorViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position));
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    public static class jugadorViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView nom;
        public jugadorViewHolder(View v){
            super(v);
            nom = (TextView) v.findViewById(R.id.tvNombre);
            nom.setTextSize(24.0f);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            final android.app.AlertDialog.Builder dialogConfirmacion = new android.app.AlertDialog.Builder(v.getContext());
            dialogConfirmacion.setTitle("Eliminar a "+nom.getText().toString());
            dialogConfirmacion.setCancelable(true);
            dialogConfirmacion.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.eliminar(nom.getText().toString());
                }
            });
            dialogConfirmacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogConfirmacion.show();
            return true;
        }

    }
}
