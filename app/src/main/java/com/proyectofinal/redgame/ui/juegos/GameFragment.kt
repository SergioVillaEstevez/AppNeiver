package com.proyectofinal.redgame.ui.juegos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyectofinal.redgame.databinding.FragmentJuegosBinding
import com.proyectofinal.redgame.ui.juegos.adapter.GameAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {


    private var _binding: FragmentJuegosBinding? = null
    private val binding get() = _binding!!

    private val juegosViewModel: GameViewModel by viewModels<GameViewModel>()

    private lateinit var juegosAdapter: GameAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuegosBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initUiState()
        initRecyclerView()
    }
    private fun initUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                juegosViewModel.game.collect() {games->
                    println("Número de juegos recibidos: ${games.size}")

                    juegosAdapter.updateList(games)

                }
            }
        }
    }
    private fun initRecyclerView() {
        juegosAdapter = GameAdapter()


        binding.recyclerViewJuegos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = juegosAdapter

        }


    }



}

