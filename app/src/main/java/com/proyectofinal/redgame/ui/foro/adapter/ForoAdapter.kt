package com.proyectofinal.redgame.ui.foro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.PostModel

class ForoAdapter (private val post : MutableList<PostModel>,private val currentUserEmail: String) : RecyclerView.Adapter<ForoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForoViewHolder {
       return ForoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent,false))


    }

    override fun getItemCount(): Int {
       return post.size
    }

    override fun onBindViewHolder(holder: ForoViewHolder, position: Int) {
        val post= post[position]
        holder.render(post)

        val cardView= holder.itemView.findViewById<CardView>(R.id.cardView)

        if (post.userEmail == currentUserEmail) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.cardPostUser)) // Color específico para el usuario
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.cardPostAll)) // Color general para otros mensajes
        }
    }

    fun updateListPost(newPost: List <PostModel>){

        post.clear()
        post.addAll(newPost)
        notifyDataSetChanged()


    }
}