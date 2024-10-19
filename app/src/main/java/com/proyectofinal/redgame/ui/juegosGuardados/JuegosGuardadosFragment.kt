package com.proyectofinal.redgame.ui.juegosGuardados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyectofinal.redgame.databinding.FragmentJuegosGuardadosBinding
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import com.proyectofinal.redgame.ui.juegosGuardados.adapter.JuegosGuardadosAdapter
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class JuegosGuardadosFragment : Fragment() {


    private lateinit var juegosGuardadosAdapter: JuegosGuardadosAdapter

    private val gameViewModel: GameViewModel by viewModels()
    private val perfilViewModel: PerfilViewModel by viewModels()


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()
        observeLikedGame()
        perfilViewModel.fetchLikedGames(gameViewModel)
    }

    private fun initRecycleView() {


        juegosGuardadosAdapter = JuegosGuardadosAdapter(mutableListOf(), perfilViewModel)



        binding.recyclerViewJuegosTodosLosJuegosGustados.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = juegosGuardadosAdapter


        }


    }

    fun observeLikedGame(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                perfilViewModel.likedGame.collect{ likedGame ->

                    juegosGuardadosAdapter.updateListLikedGame(likedGame)



                }




            }

        }

    }


}