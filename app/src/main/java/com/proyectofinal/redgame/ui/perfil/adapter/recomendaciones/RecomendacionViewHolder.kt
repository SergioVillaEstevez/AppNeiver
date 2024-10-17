package com.proyectofinal.redgame.ui.perfil.adapter.recomendaciones

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.ItemRecomendacionBinding


class RecomendacionViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRecomendacionBinding.bind(view)
    fun render(game:GameModel){


        binding.tvNombreJuegoRecomendacion.text= game.name.toString()
        Glide.with(binding.ivGameRecomendacion.context).load(game.backgroundImage).into(binding.ivGameRecomendacion)



    }

}