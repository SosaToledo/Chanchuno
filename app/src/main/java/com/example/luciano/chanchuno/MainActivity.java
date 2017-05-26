package com.example.luciano.chanchuno;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imgChancho;
    private ImageView imgFondo;
    private EditText etNombre;
    private FloatingActionButton fabAgregar;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    public static List<Jugador> jugadors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jugadors = new ArrayList<Jugador>();

        lista = (RecyclerView) findViewById(R.id.contenedor);
        lista.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new jugadorAdapter(jugadors);
        lista.setAdapter(adapter);

        etNombre = (EditText) findViewById(R.id.etNombreJugador);

    }

    public void agregar(View view) {

        String dato = etNombre.getText().toString();
        if(dato.length()==0){
            Toast.makeText(this,"ingrese algo puto", Toast.LENGTH_SHORT).show();
        } else {
            jugadors.add(new Jugador(dato));
            etNombre.setText("");
            adapter.notifyDataSetChanged();
        }

    }

    public void iniciarPartida(View view) {
        Intent intent = new Intent(this, partida.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jugadors.clear();
        adapter.notifyDataSetChanged();
    }
}
