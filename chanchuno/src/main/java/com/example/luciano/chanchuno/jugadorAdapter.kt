package com.example.luciano.chanchuno

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.luciano.chanchuno.jugadorAdapter.jugadorViewHolder
import me.toptas.fancyshowcase.FancyShowCaseView
import me.toptas.fancyshowcase.FocusShape
import me.toptas.fancyshowcase.OnViewInflateListener

/**
 * Created by luciano on 23/05/17.
 */
class jugadorAdapter(private val jugadors: MutableList<String>, contexto: Context) :
    RecyclerView.Adapter<jugadorViewHolder>(), itemClickListenerJugadores {
    private val preferences: SharedPreferences

    init {
        contexto.also { this.contexto = it }
        preferences = contexto.getSharedPreferences("config", Context.MODE_PRIVATE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): jugadorViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_nombres, parent, false)
        return jugadorViewHolder(v, this)
    }

    override fun onBindViewHolder(holder: jugadorViewHolder, position: Int) {
        holder.nom.text = jugadors.get(position)
        if (position <= 1) {
            holderGuardado = holder
        }
        if (preferences.getBoolean("tutorial01", true) && position == 1) {
            holderGuardado = holder
            val act = contexto as Activity
            val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(act.currentFocus?.windowToken, 0)
            preferences.edit().putBoolean("tutorial01", false).apply()
        }
    }

    override fun getItemCount(): Int {
        return jugadors.size
    }

    override var contexto: Context

    @SuppressLint("NotifyDataSetChanged")
    override fun itemLongClick(v: View?, position: Int) {
        jugadors.removeAt(position)
        notifyDataSetChanged()
    }

    override fun itemClick(view: View?, position: Int) {
        val builder = AlertDialog.Builder(contexto)
        val layoutInflater =
            contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater.inflate(R.layout.editar_nombre, null)
        builder.setView(v)
        val cambiarNombre = v.findViewById<View>(R.id.btnCambiarNombre) as Button
        val salir = v.findViewById<View>(R.id.btnCancelarCambiarNombre) as Button
        val campo = v.findViewById<View>(R.id.etCambiarNombre) as EditText
        val a = builder.create()
        a.setCancelable(true)
        a.show()


        //Listener for change de propertie "enabled" of botton "cambiarNombre"
        campo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if ((campo.text.toString() == "")) {
                    cambiarNombre.isEnabled = false
                } else {
                    cambiarNombre.isEnabled = true
                }
            }
        })
        cambiarNombre.setOnClickListener {
            if ((campo.getText().equals(""))) {
                a.dismiss()
            } else {
                var c = campo.text.toString().trim { it <= ' ' }
                c = c[0].uppercaseChar().toString() + c.substring(1, c.length)
                jugadors[position] = c
                notifyDataSetChanged()
            }
            a.dismiss()
        }
        salir.setOnClickListener { a.dismiss() }
    }

    class jugadorViewHolder(v: View, listener: itemClickListenerJugadores) :
        RecyclerView.ViewHolder(v), OnLongClickListener, View.OnClickListener {
        var nom: TextView
        var listener: itemClickListenerJugadores
        var editar: ImageView

        init {
            editar = v.findViewById<View>(R.id.editarNombre) as ImageView
            nom = v.findViewById<View>(R.id.tvNombre) as TextView
            nom.textSize = 24.0f
            this.listener = listener
            editar.setOnClickListener(this)
            v.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View): Boolean {
            val dialogConfirmacion = AlertDialog.Builder(v.context)
            dialogConfirmacion.setTitle("Eliminar a " + nom.text.toString())
            dialogConfirmacion.setCancelable(true)
            dialogConfirmacion.setPositiveButton("Eliminar") { dialog, which ->
                listener.itemLongClick(
                    v,
                    adapterPosition
                )
            }
            dialogConfirmacion.setNegativeButton("Cancelar") { dialog, which -> }
            dialogConfirmacion.show()
            return true
        }

        override fun onClick(v: View) {
            listener.itemClick(v, adapterPosition)
        }
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        private val contexto: Context? = null

        private var v2: FancyShowCaseView? = null

        private var holderGuardado: jugadorViewHolder? = null
        @JvmStatic
        fun obtenerCases(): FancyShowCaseView? {
            val holder = holderGuardado
            v2 = FancyShowCaseView.Builder((contexto as Activity))
                .focusOn(holder!!.nom)
                .fitSystemWindows(false)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .customView(R.layout.custom_tutorial, object : OnViewInflateListener {
                    override fun onViewInflated(view: View) {
                        val tv = view.findViewById<View>(R.id.cuerpo) as TextView
                        tv.text =
                            "Manten presionado para borrar un jugador y presiona en el lapiz para editar su nombre"
                        view.findViewById<View>(R.id.closebutton).setOnClickListener(
                            View.OnClickListener { v2!!.hide() })
                    }
                })
                .closeOnTouch(false)
                .build()
            return v2
        }
    }
}

interface itemClickListenerJugadores {
    abstract var contexto: Context

    fun itemLongClick(v: View?, position: Int)
    fun itemClick(v: View?, position: Int)
}
