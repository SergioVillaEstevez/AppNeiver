package com.proyectofinal.redgame.ui.perfil.adapter.juegosGuardados

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel

class PerfilAdapter (private var likedList: MutableList<GameModel>,private var perfilViewModel: PerfilViewModel): RecyclerView.Adapter<PerfilViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfilViewHolder {
        return PerfilViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_liked_game,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return likedList.size
    }

    override fun onBindViewHolder(holder: PerfilViewHolder, position: Int) {

        val item= likedList[position]
        holder.render(item,perfilViewModel)
    }
    // Método para actualizar la lista
    fun updateList(newLikedGames: List<GameModel>) {
        likedList.clear()
        likedList.addAll(newLikedGames)
        notifyDataSetChanged() // Notificamos al adaptador que la lista ha cambiado
    }
}
