package com.example.luciano.chanchuno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class partida extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    private TextView chancho;
    private CharSequence palabra = "CHANCHO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        chancho = (TextView) findViewById(R.id.tvchancho);

        lista= (RecyclerView) findViewById(R.id.rvPartida);
        lista.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new partidaAdapter(MainActivity.jugadors);
        lista.setAdapter(adapter);
    }

    public void cargarLetra() {
        String s = chancho.getText().toString()+palabra.charAt(chancho.getText().length());
        chancho.setText(s.subSequence(0,s.length()));
        Toast.makeText(this, "Agregar", Toast.LENGTH_SHORT).show();
        if (chancho.getText().length() == 7){
            chancho.setText("¡Chancho Va!");
        }
    }
}
