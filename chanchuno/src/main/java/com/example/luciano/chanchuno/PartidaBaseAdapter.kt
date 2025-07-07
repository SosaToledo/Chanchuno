package com.example.luciano.chanchuno

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PartidaBaseAdapter internal constructor(
    private val item: Array<Array<String?>>,
    private val context: Context
) : BaseAdapter() {
    init {
        view = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return item.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val vista = view?.inflate(R.layout.cv_partida, null)
        val imageView = vista?.findViewById<ImageView>(R.id.fotocarnet)
        val nombrejugador = vista?.findViewById<TextView>(R.id.tvNombreJugadorPartida)
        val chancho = vista?.findViewById<TextView>(R.id.tvchancho)
        nombrejugador?.text = item[position][0]
        chancho?.text = item[position][1]
        val resImagen = context.resources.getIdentifier(
            "drawable/" + item[position][2],
            null,
            context.packageName
        )
        imageView?.setImageResource(resImagen)
        return vista
    }

    companion object {
        private var view: LayoutInflater? = null
    }
}
