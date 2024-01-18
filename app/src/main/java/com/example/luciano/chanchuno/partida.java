package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class partida extends AppCompatActivity {

    private AdView adView;

    private InterstitialAd mInterstitialAd;

    private ListView lista;
    //La primera columna de la matriz contiene los nombres de los jugadores
    //La segunda columna de la matriz contiene las letras del CHANCHO
    String [][] jugadores;

    public ArrayList<String> jugadors;
    private ArrayList<String> jugadoresBackUp = new ArrayList<>();

    private final String CHANCHO_NOMBRE = "CHANCHO";
    private final String CHANCHO_PERDIO = "CHANCHO VA!";
    private PartidaBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6353529381545594/4660119647");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        MobileAds.initialize(this, "ca-app-pub-6353529381545594~4437099656");

        adView = findViewById(R.id.ad_partida);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        jugadors = getIntent().getStringArrayListExtra("jugadores");
        //Inicializo la Matriz y lugo la cargo con los jugadores y CHANCHO vacio.
        jugadores = new String[jugadors.size()][3];
        for (int i = 0; i<jugadors.size(); i++){
            jugadores[i][0] = jugadors.get(i);
            jugadores[i][1] = "";
            jugadores[i][2] = "pig1";
        }

        lista = findViewById(R.id.lv_partida);

        adapter = new PartidaBaseAdapter(jugadores, this);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tvChancho = view.findViewById(R.id.tvchancho);
                ImageView imagenChancho = view.findViewById(R.id.fotocarnet);
                TextView tvNombre = view.findViewById(R.id.tvNombreJugadorPartida);

                int posicion = tvChancho.getText().length()+1;
                if (posicion<7){
                    String prueba = CHANCHO_NOMBRE.substring(0, posicion);
                    jugadores[position][1] = prueba;
                    adapter.notifyDataSetChanged();
                    tvChancho.setText(prueba);
                    //TODO no puedo hacer que cambien las caritas
                    switch (posicion){
                        case 2:
                            imagenChancho.setImageResource(R.drawable.pig3);
                            jugadores[position][2] = "pig3";
                            break;
                        case 4:
                            imagenChancho.setImageResource(R.drawable.pig6);
                            jugadores[position][2] = "pig6";
                            break;
                        case 5:
                            imagenChancho.setImageResource(R.drawable.pig5);
                            jugadores[position][2] = "pig5";
                            break;
                    }
                }
                if (posicion==7){
                    //Se carga CHANCHO VA en el textview y en la matriz.
                    tvChancho.setText(CHANCHO_PERDIO);
                    jugadores[position][1] = CHANCHO_PERDIO;
                    adapter.notifyDataSetChanged();
                    mostrarAnuncio();
                    crearAnuncio();
                    //se crea un backup para eliminar jugadores con el fin de saber cual es el ultimo que queda
                    String jugadorEliminado = tvNombre.getText().toString();
                    jugadoresBackUp.add(jugadorEliminado);
                    jugadors.remove(jugadorEliminado);
                    if (jugadors.size()==1){
                        mostrarGanador();
                    }
                }
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvChancho = view.findViewById(R.id.tvchancho);
                int posicion = tvChancho.getText().length();
                if (posicion>0 && posicion<=7){
                    String prueba = CHANCHO_NOMBRE.substring(0, posicion-1) ;
                    tvChancho.setText(prueba);
                    jugadores[position][1] = prueba;
                    adapter.notifyDataSetChanged();
                }
                if (posicion>7){
                    tvChancho.setText(CHANCHO_NOMBRE.substring(0,6));
                }
                return true;
            }
        });

    }

    private void mostrarGanador() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View v = layoutInflater.inflate(R.layout.ganador,null);
        builder.setView(v);

        TextView nombreGanador = v.findViewById(R.id.nombreGanador);
        ImageView imagenGanador = v.findViewById(R.id.fotoGanador);
        jugadoresBackUp.add(jugadors.get(0));
        nombreGanador.setText(jugadors.get(0));
        imagenGanador.setImageResource(R.drawable.pig18);

        Button revancha = v.findViewById(R.id.revancha);
        Button salir = v.findViewById(R.id.salir);

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
                finish();
            }
        });
    }

    private void mostrarAnuncio(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    private void crearAnuncio(){
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    private void reiniciar() {
        for (int i = 0; i<jugadoresBackUp.size(); i++) {
            jugadores[i][0] = jugadoresBackUp.get(i);
            jugadores[i][1] = "";
            jugadores[i][2] = "pig1";
        }
        jugadors.clear();
        jugadors.addAll(jugadoresBackUp);
        adapter.notifyDataSetChanged();
        //Por alguna extraña razón que desconozco clear debe ir despues de la norificación.
        jugadoresBackUp.clear();

    }

}

