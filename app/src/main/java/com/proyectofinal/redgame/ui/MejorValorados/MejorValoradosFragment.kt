package com.proyectofinal.redgame.ui.MejorValorados

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyectofinal.redgame.databinding.FragmentMejorValoradosBinding
import com.proyectofinal.redgame.ui.MejorValorados.adapter.MejorValoradosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MejorValoradosFragment : Fragment() {

    private val mejorValoradosViewModel: MejorValoradosViewModel by viewModels()
    private lateinit var mejorValoradosAdapter: MejorValoradosAdapter


    private var _binding: FragmentMejorValoradosBinding? = null
    private val binding get() = _binding!!

    private var isLoading = false // Para manejar el estado de carga
    private var currentPage = 1 // Para manejar la paginación
    private val pageSize = 10 // Tamaño de la página


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
      _binding = FragmentMejorValoradosBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeLikedGame()
        mejorValoradosViewModel.fetchTopValoracionJuegos()
        setupScrollListener()



    }



    private fun initRecyclerView() {

         mejorValoradosAdapter= MejorValoradosAdapter(mutableListOf())

        binding.recyclerViewTodosMejorValorados.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter= mejorValoradosAdapter
        }


    }

    fun observeLikedGame(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                mejorValoradosViewModel.valoracionJuego.collect{ valoracionJuego ->

                    mejorValoradosAdapter.updateListRecomendacion(valoracionJuego)



                }




            }

        }

    }

    private fun loadGames() {
        if (isLoading) return  // Evitar que se carguen más juegos mientras ya estamos cargando

        isLoading = true  // Indicamos que estamos cargando juegos
        mejorValoradosViewModel.fetchTopValoracionJuegos()  // Cargar más juegos desde el ViewModel
        isLoading = false  // Ya no estamos cargando
    }


    private fun setupScrollListener() {
        binding.recyclerViewTodosMejorValorados.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Si estamos en la última posición y no estamos cargando más juegos, cargamos más
                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                    loadGames()  // Cargar más juegos
                }
            }
        })
    }



}




