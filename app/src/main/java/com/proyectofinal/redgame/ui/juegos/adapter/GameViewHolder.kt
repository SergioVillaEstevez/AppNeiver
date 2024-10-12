package com.proyectofinal.redgame.ui.juegos.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.ItemGameBinding

class GameViewHolder (view: View) : RecyclerView.ViewHolder(view){

    private val binding= ItemGameBinding.bind(view)

    fun render(gameModel: GameModel){

        val context= binding.tvNombreJuego.context

        binding.tvNombreJuego.text = gameModel.name
        binding.tvValoracion.text= gameModel.rating.toString()

        Glide.with(binding.ivGame.context).load(gameModel.backgroundImage).into(binding.ivGame)

        val id= gameModel.id.toString()

        binding.ivGame.setOnClickListener {
            Toast.makeText(
                binding.ivGame.context,
                id,
                Toast.LENGTH_LONG
            ).show()
        }


    }





}