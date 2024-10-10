package com.proyectofinal.redgame.ui.juegos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel

class GameAdapter(private var gameList: List<GameModel> = emptyList()) :
    RecyclerView.Adapter<GameViewHolder>() {

        fun updateList(list: List<GameModel>){

            gameList = list
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

        val item= gameList[position]
        holder.render(item)

    }
}