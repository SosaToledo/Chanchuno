package com.example.luciano.chanchuno;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by luciano on 23/05/17.
 */

public class jugadorAdapter extends RecyclerView.Adapter<jugadorAdapter.jugadorViewHolder> implements itemClickListenerJugadores {

    private final Context contexto;
    private List<String> jugadors;


    public jugadorAdapter(List<String> jugadors, Context contexto) {
        this.jugadors = jugadors;
        this.contexto = contexto;
    }

    @Override
    public jugadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nombres,parent,false);
        return new jugadorViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(jugadorViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position));
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    @Override
    public void itemLongClick(View v, int position) {
        jugadors.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void itemClick(View view, final int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(contexto);
        LayoutInflater layoutInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.editar_nombre,null);
        builder.setView(v);

        Button cambiarNombre = (Button)v.findViewById(R.id.btnCambiarNombre);
        Button salir = (Button) v.findViewById(R.id.btnCancelarCambiarNombre);
        final EditText campo = (EditText) v.findViewById(R.id.etCambiarNombre);

        final android.app.AlertDialog a = builder.create();
        a.setCancelable(true);
        a.show();

        cambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campo.getText().equals("")){
                    a.dismiss();}
                else {
                    String c = campo.getText().toString().trim();
                    c=Character.toUpperCase(c.charAt(0))+c.substring(1,c.length());
                    jugadors.set(position,c);
                    notifyDataSetChanged();
                }
                a.dismiss();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.dismiss();
            }
        });
    }


    public static class jugadorViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public TextView nom;
        public itemClickListenerJugadores listener;
        public ImageView editar;
        public jugadorViewHolder(View v, itemClickListenerJugadores listener){
            super(v);
            editar = (ImageView) v.findViewById(R.id.editarNombre);
            nom = (TextView) v.findViewById(R.id.tvNombre);
            nom.setTextSize(24.0f);
            this.listener = listener;
            editar.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(final View v) {
            final android.app.AlertDialog.Builder dialogConfirmacion = new android.app.AlertDialog.Builder(v.getContext());
            dialogConfirmacion.setTitle("Eliminar a "+nom.getText().toString());
            dialogConfirmacion.setCancelable(true);
            dialogConfirmacion.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.itemLongClick(v, getAdapterPosition());
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

        @Override
        public void onClick(View v) {
            listener.itemClick(v,getAdapterPosition());
        }
    }
}
interface itemClickListenerJugadores{
    void itemLongClick(View v, int position);
    void itemClick(View v,int position);
}
