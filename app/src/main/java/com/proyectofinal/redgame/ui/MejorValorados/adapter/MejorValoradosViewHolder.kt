package com.proyectofinal.redgame.ui.MejorValorados.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.FragmentMejorValoradosBinding
import com.proyectofinal.redgame.databinding.ItemTodosMejoresValoradosBinding

class MejorValoradosViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private var binding = ItemTodosMejoresValoradosBinding.bind(view)


    fun render(gameModel: GameModel){

        binding.tvNombreJuegoTodosRecomendacion.text= gameModel.name.toString()
        Glide.with(binding.ivGameTodosRecomendacion.context).load(gameModel.backgroundImage).into(binding.ivGameTodosRecomendacion)

        binding.cardViewTodosRecomendacion.setOnClickListener(){



            val plataformNames= gameModel.platforms?.joinToString(",") { platform ->
                platform.platform.name
            }
            Toast.makeText(binding.cardViewTodosRecomendacion.context, "Plataformas:" + plataformNames, Toast.LENGTH_SHORT).show()

        }


    }



}


