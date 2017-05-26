package com.example.luciano.chanchuno;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by luciano on 23/05/17.
 */

public class jugadorAdapter extends RecyclerView.Adapter<jugadorAdapter.jugadorViewHolder> {

    private List<Jugador> jugadors;

    public jugadorAdapter(List<Jugador> jugadors) {
        this.jugadors = jugadors;
    }

    @Override
    public jugadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nombres,parent,false);
        return new jugadorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(jugadorViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    public static class jugadorViewHolder extends RecyclerView.ViewHolder{
        public TextView nom;
        public jugadorViewHolder(View v){
            super(v);
            nom = (TextView) v.findViewById(R.id.tvNombre);
        }
    }
}
