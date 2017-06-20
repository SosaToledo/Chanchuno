package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre;
    private Button btnagregar;
    private FloatingActionButton btncomenzar;

    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    public static ArrayList<String> jugadors = new ArrayList<>();
    FancyShowCaseView v1,v2,v3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jugadors = new ArrayList<>();

        lista = (RecyclerView) findViewById(R.id.contenedor);
        lista.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new jugadorAdapter(jugadors);
        lista.setAdapter(adapter);

        etNombre = (EditText) findViewById(R.id.etNombreJugador);
        btnagregar = (Button)findViewById(R.id.btnAgregar);
        btncomenzar = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        llamarAlTutorial();
    }

    private void llamarAlTutorial() {
        v1 = new FancyShowCaseView.Builder(this)
            .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                @Override
                public void onViewInflated(View view) {
                    int t = 1;
                    TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                    String s ="tutorial0"+t;
                    int id = getResources().getIdentifier(s,"string",getPackageName());
                    tv.setText(id);
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
        v2 = new FancyShowCaseView.Builder(this)
                .focusOn(btnagregar)
                .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                        tv.setText(R.string.tutorial02);
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
        v3 = new FancyShowCaseView.Builder(this)
                .focusOn(btncomenzar)
                .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                        tv.setText(R.string.tutorial03);
                        view.findViewById(R.id.closebutton).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v3.hide();
                            }
                        });
                    }
                })
                .closeOnTouch(false)
                .build();

        new FancyShowCaseQueue ().add(v1).add(v2).add(v3).show();
    }

    public void agregar(View view) {
        if(etNombre.getText().toString().length()==0){
            Toast.makeText(this,"Ingrese algun nombre de jugador", Toast.LENGTH_SHORT).show();
        }else{
            String c = etNombre.getText().toString().trim();
            c=Character.toUpperCase(c.charAt(0))+c.substring(1,c.length());
            if (jugadors.size()==12){
                Toast.makeText(this, "Maximo 12 jugadores", Toast.LENGTH_SHORT).show();
            }else{
                if (!jugadors.contains(c)){
                    jugadors.add(c);
                    etNombre.setText("");
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this, etNombre.getText().toString()+" ya existe en la partida", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void iniciarPartida(View view) {
        if (jugadors.size()<2){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            if (jugadors.size()==0){
                dialog.setTitle("Vos tenes problemitas");
                dialog.setMessage("Ingresa al menos dos jugadores");
            }
            if (jugadors.size()==1){
                dialog.setTitle("Ah pero sos loco");
                dialog.setMessage("¿Cómo vas a jugar solo?");
            }
            dialog.setCancelable(true);
            dialog.setPositiveButton("Esta bien", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }else {
            Intent intent = new Intent(this, partida.class);
            intent.putExtra("jugadores", jugadors);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jugadors.clear();
        adapter.notifyDataSetChanged();
    }

    public static void eliminar(String nombre) {
        jugadors.remove(nombre);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.despedida,null);
            builder.setView(v);

            Button quedarse = (Button)v.findViewById(R.id.btnQuedarse);
            Button salir = (Button) v.findViewById(R.id.btnIrse);

            final AlertDialog a = builder.create();
            a.setCancelable(true);
            a.show();

            quedarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.dismiss();
                    Toast.makeText(MainActivity.this, "¡Te queremos :) !", Toast.LENGTH_SHORT).show();
                }
            });
            salir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.finish();
                }
            });
        }
        // para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
}
