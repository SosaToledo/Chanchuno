package com.example.luciano.chanchuno;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre;
    private Button btnagregar;

    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    public static ArrayList<String> jugadors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jugadors = new ArrayList<String>();

        lista = (RecyclerView) findViewById(R.id.contenedor);
        lista.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new jugadorAdapter(jugadors);
        lista.setAdapter(adapter);

        etNombre = (EditText) findViewById(R.id.etNombreJugador);
        btnagregar = (Button)findViewById(R.id.btnAgregar);
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
                if (!jugadors.contains((String)c)){
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
