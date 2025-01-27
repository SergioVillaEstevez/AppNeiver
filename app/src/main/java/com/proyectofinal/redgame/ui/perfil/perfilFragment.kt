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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.databinding.FragmentPerfilBinding
import com.proyectofinal.redgame.ui.juegos.CompartirViewModel
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import com.proyectofinal.redgame.ui.perfil.adapter.ListaUsuarios.ListaUsuariosAdapter
import com.proyectofinal.redgame.ui.perfil.adapter.juegosGuardados.PerfilAdapter
import com.proyectofinal.redgame.ui.perfil.adapter.recomendaciones.RecomendacionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class perfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private val binding get() = _binding!!

    private lateinit var perfilAdapter: PerfilAdapter


    private lateinit var recomendacionAdapter: RecomendacionAdapter

    private lateinit var listaUsuariosAdapter: ListaUsuariosAdapter


    private val perfilViewModel: PerfilViewModel by viewModels()

    private val gameViewModel: GameViewModel by viewModels()

    private val compartirViewModel: CompartirViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(layoutInflater, container, false)



        // cargar fragment ver todos los juegos guardados
        binding.btnVerTodo.setOnClickListener(){
            println("hola mundo")
            findNavController().navigate(R.id.action_perfilFragment_to_juegosGuardadosFragment)

        }
        binding.btnVerTodoRecomendado.setOnClickListener(){
            println("hola mundo")
            findNavController().navigate(R.id.action_perfilFragment_to_MejorValoradosFragment)

        }


        return binding.root




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeLikedGame()
        observeTopValoracionJuego()
        observeListaUsuarios()
        perfilViewModel.fetchLikedGames(gameViewModel)
        perfilViewModel.fetchTopValoracionJuegos()
        perfilViewModel.fetchListaUsuarios()
        setUsernameInTextView()








    }

    private fun initRecyclerView() {
        perfilAdapter = PerfilAdapter(mutableListOf(), perfilViewModel,compartirViewModel)

        recomendacionAdapter= RecomendacionAdapter(mutableListOf(),perfilViewModel)

        listaUsuariosAdapter = ListaUsuariosAdapter(mutableListOf(), perfilViewModel)



        binding.recyclerViewPerfilRecomendaciones.apply {
            layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter= recomendacionAdapter
        }


        binding.recyclerViewPerfilJuegosGuardados.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = perfilAdapter
        }

        binding.recyclerViewPerfilUsuarios.apply {
            layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter= listaUsuariosAdapter
        }


    }
    private fun observeTopValoracionJuego(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                perfilViewModel.topValoracionJuego.collect(){ topValoracionJuego->


                    recomendacionAdapter.updateListRecomendacion(topValoracionJuego)

                }
            }
        }



    }

    private fun observeLikedGame() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Cambiar compartirViewModel por perfilViewModel si likedGame está en perfilViewModel
                compartirViewModel.likedGame.collect { likedGames ->
                    Log.d("PerfilFragment", "Liked games count: ${likedGames.size}")

                    updateButtonStatesInGameViewModel(likedGames)

                    perfilAdapter.updateList(likedGames)

                }
            }
        }
    }


    private fun observeListaUsuarios() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                perfilViewModel.listaUsuarios.collect { usuarios ->
                    if (usuarios.isNotEmpty()) {

                        listaUsuariosAdapter.updateList(usuarios)

                    } else {
                        Log.d("PerfilFragment", "No hay usuarios disponibles.")
                    }
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


        _binding = null

    }

    private fun setUsernameInTextView() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("Usuarios").document(userEmail)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Obtén el campo "nombre_usuario" del documento
                        val username = document.getString("nombre_usuario")
                        binding.tvNombreUsuario.text = username ?: "Sin nombre de usuario"
                    } else {
                        binding.tvNombreUsuario.text = "Usuario no encontrado"
                    }
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    binding.tvNombreUsuario.text = "Error al obtener usuario"
                }
        } else {
            binding.tvNombreUsuario.text = "No autenticado"
        }
    }


}