package com.example.luciano.chanchuno

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.luciano.chanchuno.jugadorAdapter.Companion.obtenerCases
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView

class MainActivity : AppCompatActivity() {
    private var etNombre: EditText? = null
    private var btnagregar: Button? = null
    private var btncomenzar: FloatingActionButton? = null
    private val TAG = "MIAPP"
    private var adapter: jugadorAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var lista: RecyclerView? = null
    private var mAdView: AdView? = null
    private var v1: FancyShowCaseView? = null
    private var v2: FancyShowCaseView? = null
    private var v3: FancyShowCaseView? = null
    private var preferences: SharedPreferences? = null
    private var toolbar: Toolbar? = null
    private var borrar = false
    private var mIntersitialAd: InterstitialAd? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_general, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.menuItem01 -> {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                try {
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: NullPointerException) {
                    Log.d(TAG, "onOptionsItemSelected: NullpointerException: " + e.message)
                }
                val handler = Handler()
                handler.postDelayed({
                    if (adapter!!.itemCount > 0) {
                        tutorialcompleto()
                    } else {
                        llamarAlTutorial()
                    }
                }, 1000)
                preferences!!.edit()
                    .putBoolean("tutorial02", true)
                    .commit()
                true
            }

            R.id.menuItem02 -> {
                val intent = Intent(this, comoJugar::class.java)
                startActivity(intent)
                true
            }

            R.id.menuItem03 -> {
                val builder = AlertDialog.Builder(this)
                val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val v = layoutInflater.inflate(R.layout.quienessomos, null)
                builder.setView(v)
                val bt = v.findViewById<View>(R.id.twitterLogo) as ImageView
                val bf = v.findViewById<View>(R.id.facebookLogo) as ImageView
                val a = builder.create()
                a.setCancelable(true)
                a.show()
                bt.setOnClickListener {
                    val uri = Uri.parse("https://twitter.com/Think_In_Code")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                bf.setOnClickListener {
                    val uri = Uri.parse("https://www.facebook.com/thinkincode")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                super.onOptionsItemSelected(item)
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun tutorialcompleto() {
        v1 = FancyShowCaseView.Builder(this)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial01)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v1!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        v2 = FancyShowCaseView.Builder(this)
            .focusOn(btnagregar)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial02)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v2!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        v3 = FancyShowCaseView.Builder(this)
            .focusOn(btncomenzar)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial03)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v3!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        FancyShowCaseQueue().add(v1).add(v2).add(obtenerCases()).add(v3).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        jugadors = ArrayList()
        lista = findViewById<View>(R.id.contenedor) as RecyclerView
        lista!!.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        lista!!.layoutManager = layoutManager
        adapter = jugadorAdapter(jugadors, this)
        lista!!.adapter = adapter
        etNombre = findViewById<View>(R.id.etNombreJugador) as EditText
        btnagregar = findViewById<View>(R.id.btnAgregar) as Button
        btncomenzar = findViewById<View>(R.id.floatingActionButton2) as FloatingActionButton
        preferences = getSharedPreferences("config", MODE_PRIVATE)
        val inicio = preferences?.getBoolean("tutorial00", true)
        if (inicio == true) {
            llamarAlTutorial()
            preferences?.edit()?.putBoolean("tutorial00", false)?.apply()
        }
        println(adapter!!.itemCount.toString() + " adapter")

        //Publicidad
        // Sample AdMob app ID: ca-app-pub-6353529381545594~4437099656
        //MobileAds.initialize(this, "ca-app-pub-6353529381545594~4437099656")

        //Carga de Banner
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)

        //Carga de Intersitial
        InterstitialAd.load(this, "ca-app-pub-6353529381545594/8812696345", adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError?.toString()?.let { Log.d(TAG, it) }
                var mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                val d = Log.d(TAG, "Ad was loaded.".toString())
                var mInterstitialAd = interstitialAd
            }
        })
        /*mIntersitialAd = InterstitialAd(this)
        mIntersitialAd!!.adUnitId = "ca-app-pub-6353529381545594/8812696345"
        val request = AdRequest.Builder().build()
        mIntersitialAd!!.loadAd(request)*/
    }

    private fun llamarAlTutorial() {
        v1 = FancyShowCaseView.Builder(this)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial01)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v1!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        v2 = FancyShowCaseView.Builder(this)
            .focusOn(btnagregar)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial02)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v2!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        v3 = FancyShowCaseView.Builder(this)
            .focusOn(btncomenzar)
            .customView(R.layout.custom_tutorial) { view ->
                val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                tv.setText(R.string.tutorial03)
                view.findViewById<View>(R.id.closebutton).setOnClickListener { v3!!.hide() }
            }
            .closeOnTouch(false)
            .build()
        FancyShowCaseQueue().add(v1).add(v2).add(v3).show()
    }

    fun agregar(view: View?) {
        if (etNombre!!.text.toString().length == 0) {
            Toast.makeText(this, "Ingrese algun nombre de jugador", Toast.LENGTH_SHORT).show()
        } else {
            var c = etNombre!!.text.toString().trim { it <= ' ' }
            c = c[0].uppercaseChar().toString() + c.substring(1, c.length)
            if (jugadors.size == 12) {
                Toast.makeText(this, "Maximo 12 jugadores", Toast.LENGTH_SHORT).show()
            } else {
                if (!jugadors.contains(c)) {
                    jugadors.add(c)
                    etNombre!!.setText("")
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this,
                        etNombre!!.text.toString() + " ya existe en la partida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun iniciarPartida(view: View?) {
        if (jugadors.size < 2) {
            val dialog = AlertDialog.Builder(this)
            if (jugadors.size == 0) {
                dialog.setTitle("Vos tenes problemitas")
                dialog.setMessage("Ingresa al menos dos jugadores")
            }
            if (jugadors.size == 1) {
                dialog.setTitle("Ah pero sos loco")
                dialog.setMessage("¿Cómo vas a jugar solo?")
            }
            dialog.setCancelable(true)
            dialog.setPositiveButton("Esta bien") { dialog, which -> }
            dialog.show()
        } else {
            borrar = true
            val intent = Intent(this, partida::class.java)
            intent.putExtra("jugadores", jugadors)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (borrar) {
            jugadors.clear()
            adapter!!.notifyDataSetChanged()
            borrar = false
        }
    }

    fun eliminar(nombre: String) {
        jugadors.remove(nombre)
        adapter!!.notifyDataSetChanged()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val builder = AlertDialog.Builder(this)
            val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = layoutInflater.inflate(R.layout.despedida, null)
            builder.setView(v)
            val quedarse = v.findViewById<View>(R.id.btnQuedarse) as Button
            val salir = v.findViewById<View>(R.id.btnIrse) as Button
            val a = builder.create()
            a.setCancelable(true)
            a.show()
            quedarse.setOnClickListener {
                a.dismiss()
                Toast.makeText(this@MainActivity, "¡Te queremos :) !", Toast.LENGTH_SHORT).show()
            }
            salir.setOnClickListener { finish() }
        }
        // para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        var jugadors = ArrayList<String>()
        var t0 = false
        var t1 = false
        var t2 = false
    }
}
