package com.example.luciano.chanchuno

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class partida : AppCompatActivity() {
    private var lista: ListView? = null


    //La primera columna de la matriz contiene los nombres de los jugadores
    //La segunda columna de la matriz contiene las letras del CHANCHO
    lateinit var jugadores: Array<Array<String?>>
    var jugadors: ArrayList<String>? = null
    private val jugadoresBackUp = ArrayList<String>()
    private val CHANCHO_NOMBRE = "CHANCHO"
    private val CHANCHO_PERDIO = "CHANCHO VA!"
    private var adapter: PartidaBaseAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partida)


        jugadors = intent.getStringArrayListExtra("jugadores")
        //Inicializo la Matriz y lugo la cargo con los jugadores y CHANCHO vacio.
        jugadores = Array(jugadors?.size!!) { arrayOfNulls(3) }
        for (i in jugadors?.indices!!) {
            jugadores[i][0] = jugadors?.get(i)
            jugadores[i][1] = ""
            jugadores[i][2] = "pig1"
        }
        lista = findViewById(R.id.lv_partida)
        adapter = PartidaBaseAdapter(jugadores, this)
        lista?.setAdapter(adapter)
        lista?.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val tvChancho = view.findViewById<TextView>(R.id.tvchancho)
            val imagenChancho = view.findViewById<ImageView>(R.id.fotocarnet)
            val tvNombre = view.findViewById<TextView>(R.id.tvNombreJugadorPartida)
            val posicion = tvChancho.text.length + 1
            if (posicion < 7) {
                val prueba = CHANCHO_NOMBRE.substring(0, posicion)
                jugadores[position][1] = prueba
                adapter!!.notifyDataSetChanged()
                tvChancho.text = prueba
                when (posicion) {
                    2 -> {
                        imagenChancho.setImageResource(R.drawable.pig3)
                        jugadores[position][2] = "pig3"
                    }

                    4 -> {
                        imagenChancho.setImageResource(R.drawable.pig6)
                        jugadores[position][2] = "pig6"
                    }

                    5 -> {
                        imagenChancho.setImageResource(R.drawable.pig5)
                        jugadores[position][2] = "pig5"
                    }
                }
            }
            if (posicion == 7) {
                //Se carga CHANCHO VA en el textview y en la matriz.
                tvChancho.text = CHANCHO_PERDIO
                jugadores[position][1] = CHANCHO_PERDIO
                adapter!!.notifyDataSetChanged()
                //se crea un backup para eliminar jugadores con el fin de saber cual es el ultimo que queda
                val jugadorEliminado = tvNombre.text.toString()
                jugadoresBackUp.add(jugadorEliminado)
                jugadors?.remove(jugadorEliminado)
                if (jugadors?.size == 1) {
                    mostrarGanador()
                }
            }
        }
        lista?.setOnItemLongClickListener { _, view, position, _ ->
            val tvChancho = view.findViewById<TextView>(R.id.tvchancho)
            val posicion = tvChancho.text.length
            if (posicion in 1..7) {
                val prueba = CHANCHO_NOMBRE.substring(0, posicion - 1)
                tvChancho.text = prueba
                jugadores[position][1] = prueba
                adapter!!.notifyDataSetChanged()
            }
            if (posicion > 7) {
                tvChancho.text = CHANCHO_NOMBRE.substring(0, 6)
            }
            true
        }
    }

    private fun mostrarGanador() {
        val builder = AlertDialog.Builder(this)
        val layoutInflater = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val v = layoutInflater.inflate(R.layout.ganador, null)
        builder.setView(v)
        val nombreGanador = v.findViewById<TextView>(R.id.nombreGanador)
        val imagenGanador = v.findViewById<ImageView>(R.id.fotoGanador)
        jugadoresBackUp.add(jugadors!![0])
        nombreGanador.text = jugadors!![0]
        imagenGanador.setImageResource(R.drawable.pig18)
        val revancha = v.findViewById<Button>(R.id.revancha)
        val salir = v.findViewById<Button>(R.id.salir)
        val a = builder.create()
        a.setCancelable(false)
        a.show()
        revancha.setOnClickListener {
            reiniciar()
            a.dismiss()
        }
        salir.setOnClickListener { finish() }
    }

    private fun reiniciar() {
        for (i in jugadoresBackUp.indices) {
            jugadores[i][0] = jugadoresBackUp[i]
            jugadores[i][1] = ""
            jugadores[i][2] = "pig1"
        }
        jugadors!!.clear()
        jugadors!!.addAll(jugadoresBackUp)
        adapter!!.notifyDataSetChanged()
        //Por alguna extraña razón que desconozco clear debe ir despues de la notificación.
        jugadoresBackUp.clear()
    }
}
