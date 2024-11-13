package com.proyectofinal.redgame.ui.perfil.adapter.juegosGuardados

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.ItemLikedGameBinding
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel


class PerfilViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLikedGameBinding.bind(view)
    fun render(gameLikedModel: GameModel, perfilViewModel: PerfilViewModel) {
        //llamo a componentes de mi item
        binding.tvNombreJuego.text = gameLikedModel.name.toString()
        binding.tvValoracion.text = gameLikedModel.rating.toString()
        Glide.with(binding.ivGame.context).load(gameLikedModel.backgroundImage).into(binding.ivGame)
        Log.d("PerfilViewHolder", "Cargando imagen desde URL: ${gameLikedModel.backgroundImage}")
        Log.d("PerfilViewHolder", "Rendering game: ${gameLikedModel.name}")

        binding.btnEliminar.setOnClickListener(){


            if (gameLikedModel.isLiked) {
                // Añadir a la lista de "Me gusta"



                perfilViewModel.removeLikedGame(gameLikedModel)

                Log.d("PerfilViewHolder", "El juego ${gameLikedModel.name.toString()} se ha eliminado y ahora es ${gameLikedModel.isLiked}")

            }
        }


        }



    }

