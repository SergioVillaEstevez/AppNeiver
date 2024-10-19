package com.proyectofinal.redgame.ui.juegosGuardados.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.LikedGame
import com.proyectofinal.redgame.ui.juegosGuardados.JuegosGuardadosViewModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel

class JuegosGuardadosAdapter (private var likedGamelist : MutableList<GameModel>, private val perfilViewModel: PerfilViewModel) : RecyclerView.Adapter<JuegosGuardadosViewHolder>() {

   fun updateListLikedGame(newGame: List<GameModel>){

       likedGamelist.clear()
       likedGamelist.addAll(newGame)
       notifyDataSetChanged()

   }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegosGuardadosViewHolder {
        return JuegosGuardadosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todos_games_guardados,parent,false))
    }

    override fun getItemCount(): Int {
        return  likedGamelist.size
    }

    override fun onBindViewHolder(holder: JuegosGuardadosViewHolder, position: Int) {
        val item= likedGamelist[position]
        holder.render(item,perfilViewModel)

    }


}