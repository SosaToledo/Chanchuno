package com.example.luciano.chanchuno

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri


class MainActivity : AppCompatActivity() {
    private var etNombre: EditText? = null
    private var btnagregar: Button? = null
    private var btncomenzar: FloatingActionButton? = null
    private var adapter: jugadorAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var lista: RecyclerView? = null
    private var toolbar: Toolbar? = null
    private var borrar = false
    private var jugadors = ArrayList<String>()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_general, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
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
                    val uri = "https://twitter.com/Think_In_Code".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                bf.setOnClickListener {
                    val uri = "https://www.facebook.com/thinkincode".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                super.onOptionsItemSelected(item)
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        jugadors = ArrayList()
        lista = findViewById<View>(R.id.contenedor) as RecyclerView
        //TODO: revisar mejor como funciona el setHasFixedSize
        lista!!.setHasFixedSize(false)
        layoutManager = LinearLayoutManager(this)
        lista!!.layoutManager = layoutManager
        adapter = jugadorAdapter(jugadors, this)
        lista!!.adapter = adapter
        etNombre = findViewById<View>(R.id.etNombreJugador) as EditText
        btnagregar = findViewById<View>(R.id.btnAgregar) as Button
        btncomenzar = findViewById<View>(R.id.floatingActionButton2) as FloatingActionButton
    }

    @SuppressLint("NotifyDataSetChanged")
    fun agregar(view: View?) {
        if (etNombre!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingres algun nombre de jugador", Toast.LENGTH_SHORT).show()
        } else {
            var playerName = etNombre!!.text.toString().trim()
            playerName = playerName[0].uppercaseChar().toString() + playerName.substring(1, playerName.length)
            if (jugadors.size == 12) {
                Toast.makeText(this, "Maximo 12 jugadores", Toast.LENGTH_SHORT).show()
            } else {
                if (!jugadors.contains(playerName)) {
                    jugadors.add(playerName)
                    etNombre!!.setText("")
                    adapter?.notifyDataSetChanged()
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
            adapter?.notifyDataSetChanged()
            borrar = false
        }
    }
}
