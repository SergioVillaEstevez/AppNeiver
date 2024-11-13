package com.proyectofinal.redgame.ui.perfil

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(

    private var gameService : GameService
) : ViewModel() {




    private var _likedGame = MutableStateFlow<List<GameModel>>(emptyList())
    val likedGame: StateFlow<List<GameModel>> = _likedGame

    private var _topValoracionJuego = MutableStateFlow<List<GameModel>>(emptyList())
    val topValoracionJuego: StateFlow<List<GameModel>> = _topValoracionJuego



    //private var gameService = GameService()

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid // Obtener el ID del usuario

    // Función para añadir un juego a la lista de "me gusta" y actualizar Firestore
    fun addLikedGame(game: GameModel) {
        viewModelScope.launch {


            val currentList = _likedGame.value.toMutableList()
            if (!currentList.any { it.id == game.id }) {
                game.isLiked = true // Asegúrate de que isLiked sea verdadero
                currentList.add(game)
                _likedGame.value = currentList // Actualiza el estado local

                // Guardar en Firestore
                saveLikedGamesToFirestore(currentList)



            }
        }
    }


    // Función para eliminar un juego de la lista de "me gusta" y actualizar Firestore
    fun removeLikedGame(game: GameModel) {
        viewModelScope.launch {
            val currentList = _likedGame.value.toMutableList()

            // Eliminar el juego de la lista local
            if (currentList.removeIf { it.id == game.id }) {
                game.isLiked = false  // Establecer isLiked a false

                // Actualizar el estado visual (UI)
                _likedGame.value = currentList

                // Guardar la lista actualizada en Firestore
                saveLikedGamesToFirestore(currentList)

                // Si es necesario, también puedes actualizar Firestore directamente para reflejar el cambio
                // Esto es útil si tienes una colección de "games" donde se almacena el estado de cada juego

            }
        }
    }



    // Función para guardar la lista de juegos "gustados" en Firestore
    private fun saveLikedGamesToFirestore(games: List<GameModel>) {
        val userLikedGamesRef = db.collection("JuegosGuardados").document(userId ?: "default_user")

        userLikedGamesRef.update(mapOf("games" to games.map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "isLiked" to it.isLiked,
                "background_image" to it.backgroundImage,
                "rating" to it.rating
            )
        }))
            .addOnSuccessListener {
                println("Lista de juegos 'gustados' guardada con éxito.")
            }
            .addOnFailureListener { e ->
                println("Error al guardar la lista de juegos: ${e.message}")
            }
    }


    fun fetchLikedGames(gameViewModel: GameViewModel) {
        viewModelScope.launch {
            db.collection("JuegosGuardados").document(userId ?: "default_user")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Obtiene los juegos guardados en Firestore
                        val gamesData = document.data?.get("games") as? List<Map<String, Any>> ?: emptyList()
                        val likedGames = gamesData.map { GameModel.fromMap(it) }

                        // Actualiza el estado de los juegos guardados
                        _likedGame.value = likedGames

                        // Sincroniza el estado de la lista de juegos con la de Firestore
                        gameViewModel.getGameList().forEach { game ->
                            // Si el juego está en Firestore, se marca como 'liked'
                            likedGames.find { it.id == game.id }?.let { likedGame ->
                                game.isLiked = likedGame.isLiked
                            }
                        }

                        // Notifica a ViewModel que la lista ha cambiado
                        gameViewModel.notifyGameListChanged()
                    } else {
                        Log.d("PerfilViewModel", "No liked games found for user.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("PerfilViewModel", "Error fetching liked games: ${e.message}")
                }
        }
    }


    fun saveGameStateToDatabase(game: GameModel) {
        val userLikedGamesRef =
            db.collection("JuegosGuardados").document(userId ?: "default_user")

        // Obtener la lista actual de juegos guardados
        userLikedGamesRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Obtiene la lista actual de juegos
                val gamesData = document.get("games") as? List<Map<String, Any>> ?: emptyList()

                // Busca si el juego ya existe en la lista
                val updatedGames = gamesData.toMutableList()
                val existingGameIndex =
                    updatedGames.indexOfFirst { (it["id"] as? String) == game.id }

                if (existingGameIndex != -1) {
                    // Actualiza el juego existente
                    updatedGames[existingGameIndex] = mapOf(
                        "id" to game.id,
                        "name" to game.name,
                        "isLiked" to game.isLiked,
                        "background_image" to game.backgroundImage,
                        "rating" to game.rating
                    )
                } else {
                    // Agrega el juego nuevo a la lista
                    updatedGames.add(
                        mapOf(
                            "id" to game.id,
                            "name" to game.name,
                            "isLiked" to game.isLiked,
                            "background_image" to game.backgroundImage,
                            "rating" to game.rating
                        )
                    )
                }

                // Guarda la lista actualizada de juegos en Firestore
                userLikedGamesRef.update(mapOf("games" to updatedGames))
                    .addOnSuccessListener {
                        println("Estado del juego guardado con éxito.")
                    }
                    .addOnFailureListener { e ->
                        println("Error al guardar el estado del juego: ${e.message}")
                    }
            } else {
                println("El documento no existe.")
            }
        }.addOnFailureListener { e ->
            println("Error al obtener la lista de juegos: ${e.message}")
        }
    }


    fun fetchTopValoracionJuegos() {
        viewModelScope.launch {
            try {
                val gameValoracionList =
                    gameService.getGames(page = 1, pageSize = 10, search = "", ordering = "-rating")
                _topValoracionJuego.value = gameValoracionList
                Log.d("GameViewModel", "Games fetched: $gameValoracionList")

            } catch (e: Exception) {
                Log.e("GameViewModel", "Error fetching games: ${e.message}")
            }


        }


    }
}





