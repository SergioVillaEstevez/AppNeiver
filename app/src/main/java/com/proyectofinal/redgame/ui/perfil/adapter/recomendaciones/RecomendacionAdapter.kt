package com.proyectofinal.redgame.ui.perfil.adapter.recomendaciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel

class RecomendacionAdapter (private var gamelist : MutableList<GameModel>, private var perfilViewModel: PerfilViewModel) : RecyclerView.Adapter<RecomendacionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendacionViewHolder {

        return RecomendacionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recomendacion,parent,false))

    }

    override fun getItemCount(): Int {
        return gamelist.size
    }

    override fun onBindViewHolder(holder: RecomendacionViewHolder, position: Int) {
        var item= gamelist[position]
        holder.render(item)
    }

    fun updateListRecomendacion(newRateList: List<GameModel>){
        gamelist.clear()
        gamelist.addAll(newRateList)
        notifyDataSetChanged()

    }
}