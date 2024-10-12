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
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.data.network.GameService
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
    private lateinit var gameService: GameService

    private var currentPage = 2
    private var pageSize = 10

    var isLoading= false // comprobar si estar cargando datos


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
                juegosViewModel.game.collect() { games ->
                    println("Número de juegos recibidos: ${games.size}")

                    juegosAdapter.updateList(games)

                }
            }
        }
    }

    private fun initRecyclerView() {
        juegosAdapter = GameAdapter(mutableListOf())
        gameService = GameService()


        binding.recyclerViewJuegos.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = juegosAdapter

        }




        //scrollistener
        setupScrollListener()


    }

// FUNCION PARA CARGAR JUEGOS EN EL FRAGMENT
    private fun loadGames() {
        if(isLoading) return
        isLoading=true

        lifecycleScope.launch {
            try {
                val games = gameService.getGames(currentPage, pageSize)
                juegosAdapter.addGames(games)  // Añadir los juegos al adapter
                currentPage++
                isLoading=false// Incrementar la página para la próxima solicitud
            } catch (e: Exception) {
                // Manejar errores aquí (por ejemplo, mostrar un mensaje de error)
                println("Error al cargar los juegos: ${e.message}")
            }
        }
    }

// PETICION AL FINAL DEL FRAGMENT PARA CARGAR  MAS JUEGOS EN EL FRAGMENT
    private fun setupScrollListener() {

        binding.recyclerViewJuegos.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount= layoutManager.itemCount
                val lastVisibleItemPosition= layoutManager.findLastVisibleItemPosition()

                if(lastVisibleItemPosition == totalItemCount -1){

                    loadGames()
                }
            }
        })

    }

}





