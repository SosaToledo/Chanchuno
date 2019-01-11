package com.example.luciano.chanchuno;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre;
    private Button btnagregar;
    private FloatingActionButton btncomenzar;
    private String TAG ="MIAPP";
    private jugadorAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView lista;

    private AdView mAdView;

    public static ArrayList<String> jugadors = new ArrayList<>();
    private FancyShowCaseView v1,v2,v3;
    private SharedPreferences preferences;
    private Toolbar toolbar;

    public static boolean t0=false;
    public static boolean t1=false;
    public static boolean t2=false;

    private boolean borrar=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menuItem01:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }catch (NullPointerException e){
                    Log.d(TAG, "onOptionsItemSelected: NullpointerException: "+e.getMessage());
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount()>0){
                            tutorialcompleto();
                        }else{
                            llamarAlTutorial();
                        }
                        }
                },1000);
                preferences.edit()
                        .putBoolean("tutorial02",true)
                        .commit();
                return true;

            case R.id.menuItem02:
                Intent intent = new Intent(this, comoJugar.class);
                startActivity(intent);
                return true;

            case R.id.menuItem03:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = layoutInflater.inflate(R.layout.quienessomos,null);

                builder.setView(v);

                ImageView bt = (ImageView)v.findViewById(R.id.twitterLogo);
                ImageView bf = (ImageView)v.findViewById(R.id.facebookLogo);

                final AlertDialog a = builder.create();
                a.setCancelable(true);
                a.show();

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://twitter.com/Think_In_Code");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                bf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.facebook.com/thinkincode");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tutorialcompleto() {
        v1 = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                        tv.setText(R.string.tutorial01);
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

        new FancyShowCaseQueue ().add(v1).add(v2).add(jugadorAdapter.obtenerCases()).add(v3).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jugadors = new ArrayList<>();

        lista = (RecyclerView) findViewById(R.id.contenedor);
        lista.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(layoutManager);

        adapter = new jugadorAdapter(jugadors,this);
        lista.setAdapter(adapter);

        etNombre = (EditText) findViewById(R.id.etNombreJugador);
        btnagregar = (Button)findViewById(R.id.btnAgregar);
        btncomenzar = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        preferences = getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean inicio = preferences.getBoolean("tutorial00",true);
        if (inicio){
            llamarAlTutorial();
            preferences.edit().putBoolean("tutorial00",false).apply();
        }
        System.out.println(adapter.getItemCount()+" adapter");

        //Publicidad
        // Sample AdMob app ID: ca-app-pub-6353529381545594~4437099656
        MobileAds.initialize(this, "ca-app-pub-6353529381545594~4437099656");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Analitycs
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    private void llamarAlTutorial() {
        v1 = new FancyShowCaseView.Builder(this)
            .customView(R.layout.custom_tutorial, new OnViewInflateListener() {
                @Override
                public void onViewInflated(View view) {
                    TextView tv= (TextView) view.findViewById(R.id.cuerpo);
                    tv.setText(R.string.tutorial01);
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
            borrar = true;
            Intent intent = new Intent(this, partida.class);
            intent.putExtra("jugadores", jugadors);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (borrar){
            jugadors.clear();
            adapter.notifyDataSetChanged();
            borrar=false;
        }
    }

    public void eliminar(String nombre) {
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
