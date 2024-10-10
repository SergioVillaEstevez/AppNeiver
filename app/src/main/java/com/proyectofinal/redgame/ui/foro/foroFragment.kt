package com.proyectofinal.redgame.ui.foro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyectofinal.redgame.databinding.FragmentForoBinding


class foroFragment : Fragment() {

    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForoBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

}