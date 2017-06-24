package com.example.luciano.chanchuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.OnViewInflateListener;


public class partidaAdapter extends RecyclerView.Adapter<partidaAdapter.partidaViewHolder> implements itemClickListener{
    private static Context contexto;
    private CharSequence palabra = "CHANCHO";
    private List<String> jugadors;
    private List<Integer> numer = new ArrayList<>();
    private Map<String,String>jugadores = new HashMap<>();
    private static FancyShowCaseView v1;
    private FancyShowCaseView v2;
    private SharedPreferences preferences;
    public static partidaViewHolder holderGuardado;

    public partidaAdapter(List<String> jugadors, Context context) {
        this.jugadors = jugadors;
        this.contexto = context;
        this.preferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
    }

    @Override
    public partidaAdapter.partidaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_partida,parent,false);
        return new partidaAdapter.partidaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(final partidaAdapter.partidaViewHolder holder, final int position) {
        holder.nom.setText(jugadors.get(position));
        String s = "pig"+(conseguirNumero());
        holder.numeroChancho=s;
        int id = contexto.getResources().getIdentifier(s,"drawable",contexto.getPackageName());
        holder.foto.setImageResource(id);
        holder.chancho.setText("");
        holder.chanchoFondo.setText("CHANCHO");
        holder.foto.setAlpha(1f);
        jugadores.put(s,holder.nom.getText().toString());
        if (position<=1){
            holderGuardado = holder;
        }
        if (position==1 && preferences.getBoolean("tutorial02",true)){
            holderGuardado=holder;
            holder.chancho.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Layout has happened here.
                            lanzartutorial();
                            holder.chancho.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            // Don't forget to remove your listener when you are done with it.
                        }
                    });
            preferences.edit().putBoolean("tutorial02",false).apply();
        }
    }

    private int conseguirNumero() {
        int numero;
        Random r = new Random();
        numero=(r.nextInt(19)+1);
        if (numer.contains(numero)){
            numero=conseguirNumero();
        }
        numer.add(numero);
        return numero;
    }

    @Override
    public int getItemCount() {
        return jugadors.size();
    }

    @Override
    public void onItemClick(View v, partidaViewHolder viewHolder, int position) {
        if (viewHolder.chancho.length()<11){
            String s = viewHolder.chancho.getText().toString() + palabra.charAt(viewHolder.chancho.getText().length());
            if (s.length() >= 0) {
                viewHolder.chancho.setText(s.subSequence(0, s.length()));
            }
            if (viewHolder.chancho.getText().length() == 7) {
                viewHolder.chancho.setText("¡CHANCHO VA!");
                viewHolder.chanchoFondo.setText("");
                viewHolder.foto.setAlpha(0.5f);
                Toast.makeText(v.getContext(), viewHolder.nom.getText()+" perdio.", Toast.LENGTH_SHORT).show();
                jugadores.remove(viewHolder.numeroChancho);
                if (jugadores.size() == 1){
                    mostrarGanador();
                }

            }
        }
    }

    private void mostrarGanador() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater layoutInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.ganador,null);
        builder.setView(v);

        String numeroChanchuno, nombreChancho;
        numeroChanchuno="";
        nombreChancho="";
        Iterator<Map.Entry<String,String>> iterator = jugadores.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String,String> e = iterator.next();
            numeroChanchuno=e.getKey();
            nombreChancho=e.getValue();
        }

        TextView tv = (TextView)v.findViewById(R.id.nombreGanador);
        tv.setText("¡Gano "+nombreChancho+"!");
        System.out.println(jugadors.get(0));

        ImageView img = (ImageView)v.findViewById(R.id.fotoGanador);

        int id = contexto.getResources().getIdentifier(numeroChanchuno,"drawable",contexto.getPackageName());
        img.setImageResource(id);

        Button revancha = (Button)v.findViewById(R.id.revancha);
        Button salir = (Button) v.findViewById(R.id.salir);

        final AlertDialog a = builder.create();
        a.setCancelable(false);
        a.show();

        revancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
                a.dismiss();
            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)contexto).finish();
            }
        });
    }

    private void reiniciar() {
        jugadores.clear();
        List<String> aux = new ArrayList<>(jugadors);
        jugadors.clear();
        jugadors.addAll(aux);
        notifyDataSetChanged();
    }

    public static void lanzartutorial() {
        partidaViewHolder holder = holderGuardado;
        v1 = new FancyShowCaseView.Builder((Activity) contexto)
                .focusOn(holder.cv)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                        tv.setText("Presiona para agregar una letra y mantene apretado para quitar letras");
                        view.findViewById(R.id.closebutton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v1.hide();
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .build();
        new FancyShowCaseQueue().add(v1).show();
    }

    @Override
    public void onLongItemClick(View v, partidaViewHolder holder) {
        if (holder.chancho.length()>0 && holder.chancho.length()<11 ){
            holder.chancho.setText(holder.chancho.getText().toString().substring(0,holder.chancho.getText().toString().length()-1));
        }
        if (holder.chancho.getText().toString().equals("¡CHANCHO VA!")){
            holder.chancho.setText("CHANCH");
            holder.foto.setAlpha(1f);
            holder.chanchoFondo.setText("CHANCHO");
            jugadores.put(holder.numeroChancho,holder.nom.getText().toString());
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
        public String numeroChancho;
        public CardView cv;

        public partidaViewHolder(View v, itemClickListener listener){
            super(v);
            cv = (CardView)v.findViewById(R.id.cardboardJugador);
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
            listener.onItemClick(v, this, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongItemClick(v,this);
            return true;
        }
    }
}

interface itemClickListener{
    void onItemClick(View v, partidaAdapter.partidaViewHolder viewHolder,  int position);
    void onLongItemClick(View v, partidaAdapter.partidaViewHolder holder);
}
