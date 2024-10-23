package com.proyectofinal.redgame.ui.foro.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.data.model.PostModel
import com.proyectofinal.redgame.databinding.ItemPostBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForoViewHolder (view:View) : RecyclerView.ViewHolder(view) {

    private var binding= ItemPostBinding.bind(view)
    fun render(post: PostModel){

        binding.tvUserName.text= post.userName.toString()
        binding.tvContenido.text= post.content.toString()
        binding.tvTime.text= SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(post.timestamp))





    }

}