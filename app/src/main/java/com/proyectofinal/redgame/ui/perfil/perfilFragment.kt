package com.proyectofinal.redgame.ui.perfil

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.databinding.FragmentPerfilBinding
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import com.proyectofinal.redgame.ui.juegos.adapter.GameAdapter
import com.proyectofinal.redgame.ui.perfil.adapter.PerfilAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class perfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private lateinit var perfilAdapter: PerfilAdapter


    private val perfilViewModel: PerfilViewModel by viewModels()

    private val gameViewModel: GameViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeLikedGame()
        perfilViewModel.fetchLikedGames(gameViewModel)
    }

    private fun initRecyclerView() {
        perfilAdapter = PerfilAdapter(mutableListOf(),perfilViewModel)

        binding.recyclerViewPerfil.apply {

            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter= perfilAdapter
        }

    }


        private fun observeLikedGame(){

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    perfilViewModel.likedGame.collect(){likedGames->
                        Log.d("PerfilFragment", "Liked games count: ${likedGames.size}")
                        perfilAdapter.updateList(likedGames)
                        updateButtonStatesInGameViewModel(likedGames)
                    }
                }
            }
        }
    private fun updateButtonStatesInGameViewModel(likedGames: List<GameModel>) {
        likedGames.forEach { likedGame ->
            gameViewModel.getGameList().find { it.id == likedGame.id }?.isLiked = true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpia el binding para evitar fugas de memoria
    }









}