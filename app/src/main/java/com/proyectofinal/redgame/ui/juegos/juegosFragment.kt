package com.proyectofinal.redgame.ui.juegos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyectofinal.redgame.databinding.FragmentJuegosBinding
import com.proyectofinal.redgame.databinding.FragmentPerfilBinding



class juegosFragment : Fragment() {

    private var _binding: FragmentJuegosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuegosBinding.inflate(layoutInflater, container, false)

        return binding.root

    }


}