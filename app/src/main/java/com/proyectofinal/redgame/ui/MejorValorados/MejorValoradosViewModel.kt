package com.proyectofinal.redgame.ui.MejorValorados

import android.util.Log
import android.util.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.network.GameService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MejorValoradosViewModel @Inject constructor(

    private var gameService : GameService
) : ViewModel() {


    private var _valoracionJuego = MutableStateFlow<List<GameModel>>(emptyList())
    val valoracionJuego: StateFlow<List<GameModel>> = _valoracionJuego

    private var currentPage = 1
    private val pageSize = 10

    fun fetchTopValoracionJuegos() {
        viewModelScope.launch {
            try {
                val gameValoracionList = gameService.getGames(page = currentPage, pageSize = pageSize, search = "", ordering = "-rating")
                val updatedList = _valoracionJuego.value.orEmpty() + gameValoracionList  // Combinar los juegos actuales con los nuevos juegos
                _valoracionJuego.value = updatedList  // Actualiza la lista con los nuevos juegos
                currentPage++  // Incrementa la página para la próxima solicitud
                Log.d("GameViewModel", "Games fetched: $gameValoracionList")
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games: ${e.message}")
            }
        }
    }






}