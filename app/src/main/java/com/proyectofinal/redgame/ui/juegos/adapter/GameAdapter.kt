package com.proyectofinal.redgame.ui.juegos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel

class GameAdapter(private var gameList: MutableList<GameModel> = mutableListOf(), private var perfilViewModel: PerfilViewModel
) :
    RecyclerView.Adapter<GameViewHolder>() {





    fun addGames(newGames: List<GameModel>) {
        val startPosition = gameList.size
        gameList.addAll(newGames)
        println("los juegos son " + newGames)
        notifyItemRangeInserted(startPosition, newGames.size)  // Notificar al RecyclerView
    }

    fun updateList(list: List<GameModel>) {
        gameList.clear()
        gameList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {

        val item = gameList[position]

        holder.render(item,perfilViewModel)
        holder.updateButtonState(item.isLiked)



    }
}