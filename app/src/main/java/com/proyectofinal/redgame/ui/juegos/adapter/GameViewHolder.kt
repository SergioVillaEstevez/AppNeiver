package com.proyectofinal.redgame.ui.juegos.adapter

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.ItemGameBinding
import com.proyectofinal.redgame.ui.juegos.CompartirViewModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import com.proyectofinal.redgame.ui.perfil.perfilFragment

class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGameBinding.bind(view)


    fun render(gameModel: GameModel,perfilViewModel: PerfilViewModel,compartirViewModel: CompartirViewModel ) {



        val context = binding.tvNombreJuego.context

        binding.tvNombreJuego.text = gameModel.name
        binding.tvValoracion.text = gameModel.rating.toString()
        Glide.with(binding.ivGame.context).load(gameModel.backgroundImage).into(binding.ivGame)
        val id = gameModel.id.toString()




        //isLiked le explico que puede ser
        binding.btnMeGusta.text = if (gameModel.isLiked) "Guardado" else "+"





        binding.btnMeGusta.setOnClickListener {

            fun toggleLike(game: GameModel) {
                game.isLiked = !game.isLiked
                compartirViewModel.saveGameStateToDatabase(game) // Guardar el estado actualizado

            }


            gameModel.isLiked = !gameModel.isLiked // Cambia el estado de "Me gusta"





         if (gameModel.isLiked) {
                compartirViewModel.addLikedGame(gameModel)
         updateButtonState(true)// Añadir a la lista de "Me gusta"
            } else{
                compartirViewModel.removeLikedGame(gameModel)
             updateButtonState(false)
            }




            // Opcional: Aquí podrías guardar el estado en la base de datos o notificar al adaptador
            Log.d("GameViewHolder", "${gameModel.name} isLiked: ${gameModel.isLiked}")
        }

        binding.ivGame.setOnClickListener {
            Toast.makeText(
                binding.ivGame.context,
                id,
                Toast.LENGTH_LONG
            ).show()
        }




    }


    fun updateButtonState(isLiked: Boolean) {
        if (isLiked) {

            binding.btnMeGusta.text = "Guardado"
            binding.btnMeGusta.setBackgroundResource(R.drawable.button_like)  // Cambiar a color verde
        } else {
            binding.btnMeGusta.text = "+"
            binding.btnMeGusta.setBackgroundColor(ContextCompat.getColor(binding.btnMeGusta.context, R.color.trans)) // Cambiar a color gris
        }
    }




    }

