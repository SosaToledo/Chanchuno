package com.example.luciano.chanchuno;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.OnViewInflateListener;

/**
 * Created by luciano on 23/05/17.
 */

public class jugadorAdapter extends RecyclerView.Adapter<jugadorAdapter.jugadorViewHolder> implements itemClickListenerJugadores {

    private static Context contexto;
    private List<String> jugadors;
    private SharedPreferences preferences;
    private static FancyShowCaseView v2;
    //Guardamos un holder para usarlo en el tutorial despues
    private static jugadorViewHolder holderGuardado;

    public jugadorAdapter(List<String> jugadors, Context contexto) {
        this.jugadors = jugadors;
        this.contexto = contexto;
        this.preferences=contexto.getSharedPreferences("config",Context.MODE_PRIVATE);
    }

    @Override
    public jugadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nombres,parent,false);
        return new jugadorViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(final jugadorViewHolder holder, int position) {
        holder.nom.setText(jugadors.get(position));
        if (position<=1){
            holderGuardado = holder;
        }
        if (preferences.getBoolean("tutorial01", true) && position==1){
            holderGuardado = holder;
            Activity act= (Activity) contexto;
            InputMethodManager imm = (InputMethodManager)contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),0);
            final Handler handler = new Handler();
            holder.editar.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Layout has happened here.
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    llamarTutorial();
                                }
                            },1000);
                            holder.editar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            // Don't forget to remove your listener when you are done with it.
                        }
                    });


            preferences.edit().putBoolean("tutorial01",false).apply();
        }
    }
    public void llamarTutorial(){
        new FancyShowCaseQueue().add(obtenerCases()).show();
    }
    public static FancyShowCaseView obtenerCases(){

        jugadorViewHolder holder = holderGuardado;

        v2 = new FancyShowCaseView.Builder((Activity) contexto)
                .focusOn(holder.nom)
                .fitSystemWindows(false)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        TextView tv = (TextView) view.findViewById(R.id.cuerpo);
                        tv.setText("Manten presionado para borrar un jugador y presiona en el lapiz para editar su nombre");
                        view.findViewById(R.id.closebutton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v2.hide();
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .build();
        return v2;
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

        final Button cambiarNombre = (Button)v.findViewById(R.id.btnCambiarNombre);
        Button salir = (Button) v.findViewById(R.id.btnCancelarCambiarNombre);
        final EditText campo = (EditText) v.findViewById(R.id.etCambiarNombre);

        final android.app.AlertDialog a = builder.create();
        a.setCancelable(true);
        a.show();


        //Listener for change de propertie "enabled" of botton "cambiarNombre"
        campo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cambiarNombre.setEnabled(true);
            }
        });

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
