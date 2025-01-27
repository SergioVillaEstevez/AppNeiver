package com.proyectofinal.redgame.ui.MejorValorados.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.ui.juegosGuardados.adapter.JuegosGuardadosViewHolder

class MejorValoradosAdapter ( private var gameListValorados : MutableList<GameModel>): RecyclerView.Adapter<MejorValoradosViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MejorValoradosViewHolder {
        return MejorValoradosViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todos_mejores_valorados, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return gameListValorados.size
    }

    override fun onBindViewHolder(holder: MejorValoradosViewHolder, position: Int) {
        var item = gameListValorados[position]
        holder.render(item)
    }

    fun updateListRecomendacion(newRateList: List<GameModel>) {
        gameListValorados.clear()
        gameListValorados.addAll(newRateList)
        notifyDataSetChanged()
    }
    fun addGames(newGames: List<GameModel>) {
        val startPosition = gameListValorados.size
        gameListValorados.addAll(newGames)  // Añadir los nuevos juegos a la lista
        notifyItemRangeInserted(startPosition, newGames.size)  // Notificar al RecyclerView que nuevos ítems han sido añadidos
    }
}