package com.proyectofinal.redgame.ui.juegos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.LikedGame
import com.proyectofinal.redgame.data.network.GameService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameService: GameService
) : ViewModel() {

    private lateinit var compartirViewModel: CompartirViewModel
    private var originalGameList: List<GameModel> = emptyList()

    private var _game = MutableStateFlow<List<GameModel>>(emptyList())
    val game: StateFlow<List<GameModel>> = _game

    private lateinit var searchQuery: String

    //private val gameService = GameService()

    init {

            fetchGames("")

    }

    // Función para actualizar solo el estado 'isLiked' de un juego


    fun fetchGames(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {


                val gameList =
                    gameService.getGames(page = 1, pageSize = 10, search = search, ordering = "")
                _game.value = gameList

                originalGameList = gameList
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