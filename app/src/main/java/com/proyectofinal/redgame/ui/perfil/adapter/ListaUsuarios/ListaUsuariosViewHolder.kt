package com.proyectofinal.redgame.ui.perfil.adapter.ListaUsuarios

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.data.model.ListaUsuariosModel
import com.proyectofinal.redgame.databinding.ItemLikedGameBinding
import com.proyectofinal.redgame.databinding.ItemListUsuarioBinding
import com.proyectofinal.redgame.login.data.model.UsuarioModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import com.proyectofinal.redgame.ui.perfil.adapter.juegosGuardados.PerfilViewHolder

class ListaUsuariosViewHolder (view: View): RecyclerView.ViewHolder(view){
    private val binding = ItemListUsuarioBinding.bind(view)
    fun render(usuarioModel: ListaUsuariosModel, perfilViewModel: PerfilViewModel){

        binding.tvUserName.text= usuarioModel.nombre_usuario.toString()
        binding.tvContenido.text= usuarioModel.nombre_completo.toString()




    }

}
