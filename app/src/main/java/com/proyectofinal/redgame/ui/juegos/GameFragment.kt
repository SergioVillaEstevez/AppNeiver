package com.proyectofinal.redgame.ui.juegos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.GameProvider
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.databinding.FragmentJuegosBinding
import com.proyectofinal.redgame.ui.juegos.adapter.GameAdapter
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val perfilViewModel: PerfilViewModel by viewModels()

    private var _binding: FragmentJuegosBinding? = null
    private val binding get() = _binding!!

    private val juegosViewModel: GameViewModel by viewModels<GameViewModel>()
    private  val compartirViewModel: CompartirViewModel by viewModels()

    private lateinit var juegosAdapter: GameAdapter
    private lateinit var gameService: GameService

    private var allGames: List<GameModel> = emptyList()






    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid // Obtener el ID del usuario



    private var currentPage = 2
    private var pageSize = 10

    var isLoading = false // comprobar si estar cargando datos


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJuegosBinding.inflate(layoutInflater, container, false)

        lifecycleScope.launch {
            compartirViewModel.fetchlikegameScope()
        }
        return binding.root

    }
    override fun onResume() {
        super.onResume()
        compartirViewModel.fetchlikegameScope()  // Recargar los datos
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()

        fun normalizeString(str: String): String {
            return str.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").trim()
        }



        binding.etFilter.addTextChangedListener { userFilter ->
            val filterText = normalizeString(userFilter?.toString().orEmpty())
            Log.i("Filter", "Texto ingresado: $filterText")

            juegosViewModel.fetchGames(search = filterText)

        }



    }

//  Busqueda en la pagina 1 de 10 elementos
//   val gameFiltered = juegosViewModel.getGameList().filter { gameModel ->
//                normalizeString(gameModel.name).contains(filterText)
//            }
//            Log.i("Filter", "Juegos filtrados: ${gameFiltered.map { it.name }}")
//            Log.i("Filter", "Todos los juegos: ${juegosViewModel.game.value.map { it.name }}") // Verifica todos los juegos
//
//
//            juegosAdapter.updateListFilter(gameFiltered.toMutableList())


    private fun initUi() {
        initUiState()
        initRecyclerView()


    }

    private fun initUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                compartirViewModel.likedGame.collect { likedGames ->  // Ahora recolectamos una lista de GameModel completos
                    juegosViewModel.game.collect { games ->
                        val updatedGames = games.map { game ->

                            val isLiked = likedGames.any { it.id == game.id }  // Comparamos si el ID del juego está en la lista de juegos "gustados"
                            game.copy(isLiked = isLiked)  // Creamos una copia del juego con el estado actualizado
                        }

                        juegosAdapter.updateList(updatedGames)  // Actualizamos el RecyclerView con los juegos modificados


                    }
                }
            }
        }














}

    private fun initRecyclerView() {
        juegosAdapter = GameAdapter(mutableListOf(),perfilViewModel,compartirViewModel)
        gameService = GameService()


        binding.recyclerViewJuegos.apply {
            layoutManager = LinearLayoutManager(context)

            adapter = juegosAdapter

        }


        //scrollistener
        setupScrollListener()


    }



    // FUNCION PARA CARGAR JUEGOS EN EL FRAGMENT
    private fun loadGames() {
        if (isLoading) return

        isLoading = true

        lifecycleScope.launch() {
            try {
                val games = gameService.getGames(currentPage, pageSize, ordering = "")
                juegosAdapter.addGames(games)  // Añadir los juegos al adapter
                currentPage++
                isLoading = false// Incrementar la página para la próxima solicitud
            } catch (e: Exception) {
                // Manejar errores aquí (por ejemplo, mostrar un mensaje de error)
                println("Error al cargar los juegos: ${e.message}")
            }
        }
    }

    // PETICION AL FINAL DEL FRAGMENT PARA CARGAR  MAS JUEGOS EN EL FRAGMENT
    private fun setupScrollListener() {

        binding.recyclerViewJuegos.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition == totalItemCount - 1) {

                    loadGames()
                }
            }
        })

    }


    }







