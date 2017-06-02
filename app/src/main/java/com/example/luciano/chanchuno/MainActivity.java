package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre;
    private Button btnagregar;

    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    public static List<String> jugadors;

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

        String dato = etNombre.getText().toString();
        if(dato.length()==0){
            Toast.makeText(this,"ingrese algo puto", Toast.LENGTH_SHORT).show();
        } else {
            dato=Character.toUpperCase(dato.charAt(0))+dato.substring(1,dato.length());
            jugadors.add(dato);
            etNombre.setText("");
            adapter.notifyDataSetChanged();
        }
        if (jugadors.size()==12){
            btnagregar.setEnabled(false);
            Toast.makeText(this, "Maximo 12 jugadores", Toast.LENGTH_SHORT).show();
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
                dialog.setMessage("¿Como vas a jugar solo?");
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
        jugadors.remove((String)nombre);
        adapter.notifyDataSetChanged();
        System.out.println(nombre);
    }
}
