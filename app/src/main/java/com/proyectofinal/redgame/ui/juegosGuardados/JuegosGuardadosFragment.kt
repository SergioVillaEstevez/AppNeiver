package com.proyectofinal.redgame.ui.juegosGuardados

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.databinding.FragmentForoBinding
import com.proyectofinal.redgame.databinding.FragmentJuegosGuardadosBinding
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class JuegosGuardadosFragment : Fragment() {



    private var _binding: FragmentJuegosGuardadosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJuegosGuardadosBinding.inflate(layoutInflater, container, false)

        return binding.root


    }


}