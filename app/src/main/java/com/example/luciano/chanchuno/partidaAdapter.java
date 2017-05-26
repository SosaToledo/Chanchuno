package com.example.luciano.chanchuno;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by luciano on 23/05/17.
 */

public class partidaAdapter extends RecyclerView.Adapter<partidaAdapter.partidaViewHolder> implements ItemClickListener{
    CharSequence palabra = "CHANCHO";
    private List<Jugador> jugadors;

    public partidaAdapter(List<Jugador> jugadors) {
        this.jugadors = jugadors;
    }

    @Override
    public partidaAdapter.partidaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_partida,parent,false);
        return new partidaAdapter.partidaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(partidaAdapter.partidaViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    @Override
    public void onItemClick(partidaAdapter.partidaViewHolder holder) {
        if (holder.chancho.length()<11) {
            String s = holder.chancho.getText().toString() + palabra.charAt(holder.chancho.getText().length());
            if (s.length() >= 0) {
                holder.chancho.setText(s.subSequence(0, s.length()));
            }
            if (holder.chancho.getText().length() == 7) {
                holder.chancho.setText("¡Chancho Va!");
            }
        }
    }

    public static class partidaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nom;
        public ImageView img;
        public TextView chancho;
        public ItemClickListener listener;
        public partidaViewHolder(View v, ItemClickListener listener){
            super(v);
            nom = (TextView) v.findViewById(R.id.tvNombreJugadorPartida);
            chancho = (TextView) v.findViewById(R.id.tvchancho);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(this);
        }
    }

}

interface ItemClickListener {
    void onItemClick(partidaAdapter.partidaViewHolder holder);
}
