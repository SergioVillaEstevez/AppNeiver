package com.proyectofinal.redgame.ui.juegos

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor() : ViewModel() {

    private var _game = MutableStateFlow<List<GameModel>>(emptyList())

    val game: StateFlow<List<GameModel>> = _game

    private val gameService = GameService()

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                val gameList = gameService.getGames(page = 1, pageSize = 10)
                _game.value = gameList
                Log.d("GameViewModel", "Games fetched: $gameList")

            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games: ${e.message}")
            }
        }


    }


    fun getGameList(): List<GameModel> {
        return _game.value
    }

    fun notifyGameListChanged() {

        // al actualizar la lista de juegos.
        _game.value = _game.value  // Este es un truco para notificar que la lista ha cambiado
    }


}