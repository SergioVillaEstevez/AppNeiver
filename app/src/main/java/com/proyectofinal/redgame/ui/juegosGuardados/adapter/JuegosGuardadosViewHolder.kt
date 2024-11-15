package com.proyectofinal.redgame.ui.juegosGuardados.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.ItemTodosGamesGuardadosBinding
import com.proyectofinal.redgame.ui.juegos.CompartirViewModel
import com.proyectofinal.redgame.ui.juegosGuardados.JuegosGuardadosViewModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel

class JuegosGuardadosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemTodosGamesGuardadosBinding.bind(view)

    fun render(gameModel: GameModel, perfilViewModel: PerfilViewModel,compartirViewModel: CompartirViewModel) {

        binding.tvNombreJuego.text = gameModel.name.toString()
        binding.tvValoracion.text = gameModel.rating.toString()
        Glide.with(binding.ivGame.context).load(gameModel.backgroundImage).into(binding.ivGame)


      binding.btnEliminar.setOnClickListener(){

          if(gameModel.isLiked){

              compartirViewModel.removeLikedGame(gameModel)
          }



      }

    }





    }


