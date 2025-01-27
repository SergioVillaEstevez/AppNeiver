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

    fun render(gameModel: GameModel, perfilViewModel: PerfilViewModel, compartirViewModel: CompartirViewModel) {
        val context = binding.tvNombreJuego.context

        binding.tvNombreJuego.text = gameModel.name
        binding.tvValoracion.text = gameModel.rating.toString()
        Glide.with(binding.ivGame.context).load(gameModel.backgroundImage).into(binding.ivGame)

        // Actualiza el botón según el estado actual del juego
        updateButtonState(gameModel.isLiked)

        // Configura el clic del botón
        binding.btnMeGusta.setOnClickListener {
            toggleLike(gameModel, compartirViewModel)
        }

        binding.ivGame.setOnClickListener {
            val plataformNames= gameModel.platforms?.joinToString(",") {platform ->
                platform.platform.name
            }
            Toast.makeText(binding.ivGame.context,"Plataformas:" + plataformNames, Toast.LENGTH_LONG).show()
        }
    }

    private fun toggleLike(gameModel: GameModel, compartirViewModel: CompartirViewModel) {
        val newIsLikedState = !gameModel.isLiked // Cambia el estado
        gameModel.isLiked = newIsLikedState

        // Guarda el nuevo estado en el ViewModel
        if (newIsLikedState) {
            compartirViewModel.addLikedGame(gameModel)
        } else {
            compartirViewModel.removeLikedGame(gameModel)
        }

        // Actualiza el botón de UI para reflejar el nuevo estado
        updateButtonState(newIsLikedState)

    }

    fun updateButtonState(isLiked: Boolean) {
        if (isLiked) {
            binding.btnMeGusta.text = "Guardado"
            binding.btnMeGusta.setBackgroundResource(R.drawable.button_like)  // Cambiar a color verde
        } else {
            binding.btnMeGusta.text = "+"
            binding.btnMeGusta.setBackgroundColor(
                ContextCompat.getColor(binding.btnMeGusta.context, R.color.trans)
            ) // Cambiar a color gris
        }
    }
}
