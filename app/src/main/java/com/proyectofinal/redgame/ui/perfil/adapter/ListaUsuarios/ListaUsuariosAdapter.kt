package com.proyectofinal.redgame.ui.perfil.adapter.ListaUsuarios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.ListaUsuariosModel
import com.proyectofinal.redgame.login.data.model.UsuarioModel
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import com.proyectofinal.redgame.ui.perfil.adapter.juegosGuardados.PerfilViewHolder

class ListaUsuariosAdapter (private var usuariosList : MutableList<ListaUsuariosModel>,private var perfilViewModel: PerfilViewModel) : RecyclerView.Adapter<ListaUsuariosViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaUsuariosViewHolder {
        return ListaUsuariosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_usuario,parent,false)
        )
    }

    override fun getItemCount(): Int {
       return usuariosList.size
    }

    override fun onBindViewHolder(holder: ListaUsuariosViewHolder, position: Int) {
        val item = usuariosList[position]
        holder.render(item,perfilViewModel)
    }

    fun updateList(newLikedGames: List<ListaUsuariosModel>) {
        usuariosList.clear()
        usuariosList.addAll(newLikedGames)

        notifyDataSetChanged() // Notificamos al adaptador que la lista ha cambiado
    }

}