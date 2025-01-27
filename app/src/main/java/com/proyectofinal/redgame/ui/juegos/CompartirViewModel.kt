package com.proyectofinal.redgame.ui.juegos

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.ui.perfil.PerfilViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CompartirViewModel @Inject  constructor(
    private  var gameService: GameService

) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.email // Obtener el ID del usuario
    private var _likedGame = MutableStateFlow<List<GameModel>>(emptyList())
    val likedGame: StateFlow<List<GameModel>> = _likedGame

   // private var _topValoracionJuego = MutableStateFlow<List<GameModel>>(emptyList())
   // val topValoracionJuego: StateFlow<List<GameModel>> = _topValoracionJuego

    init {
        // Cargar la lista de juegos guardados al iniciar el ViewModel
        viewModelScope.launch {
            _likedGame.value = fetchLikedGamesFromFirestore()
            observeLikedGames()
        }
    }

    //private var gameService = GameService()
    fun observeLikedGames() {
        val userLikedGamesRef = db.collection("JuegosGuardados").document(userId ?: "default_user")

        // Usa un SnapshotListener para escuchar cambios en tiempo real
        userLikedGamesRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.w("CompartirViewModel", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val gamesData = documentSnapshot.get("games") as? List<Map<String, Any>> ?: emptyList()
                val updatedGames = gamesData.map { GameModel.fromMap(it) }

                // Actualiza el estado local
                _likedGame.value = updatedGames
            }
        }
    }


    fun fetchlikegameScope(){
    viewModelScope.launch {
        val games = fetchLikedGamesFromFirestore()
        _likedGame.value=games
    }
}

    fun updateGameLikeStatus(game: GameModel, isLiked: Boolean) {
        viewModelScope.launch {
            // Aquí puedes actualizar el estado en Firestore
            val updatedGame = game.copy(isLiked = isLiked)

            // Actualizar la lista local
            val updatedGames = _likedGame.value?.toMutableList() ?: mutableListOf()
            val index = updatedGames.indexOfFirst { it.id == game.id }
            if (index != -1) {
                updatedGames[index] = updatedGame
                _likedGame.value = updatedGames
            }

            // Guardar en Firestore
            saveLikedGamesToFirestore(updatedGames)
        }
    }


    // Función para añadir un juego a la lista de "me gusta" y actualizar Firestore

    private suspend fun fetchLikedGamesFromFirestore(): List<GameModel> {
        val likedGames = mutableListOf<GameModel>()
        val userLikedGamesRef = db.collection("JuegosGuardados").document(userId ?: "default_user")

        val document = userLikedGamesRef.get().await() // Usando corutinas
        if (document.exists()) {
            val gamesData = document.get("games") as? List<Map<String, Any>> ?: emptyList()
            likedGames.addAll(gamesData.map { GameModel.fromMap(it) })
        }
        return likedGames
    }

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

            // Verificar si el juego está en la lista y eliminarlo
            val gameToRemove = currentList.find { it.id == game.id }
            if (gameToRemove != null) {
                // Establecer el estado de "isLiked" a false para la base de datos
                gameToRemove.isLiked = false
                Log.d("CompartirViewModel", "${gameToRemove.name.toString()} // ${gameToRemove.isLiked}")

                // Eliminarlo de la lista local
                currentList.remove(gameToRemove)

                // Actualizar la lista local
                _likedGame.value = currentList

                // Guardar en Firestore
                saveLikedGamesToFirestore(currentList)

                //Recargar la lista de Firestore después de la actualizació
                val updatedGames = fetchLikedGamesFromFirestore()
                _likedGame.value = updatedGames
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




    fun saveGameStateToDatabase(game: GameModel) {
        val userLikedGamesRef = db.collection("JuegosGuardados").document(userId ?: "default_user")

        // Obtener la lista actual de juegos guardados
        userLikedGamesRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Obtiene la lista actual de juegos (Asegúrate de que sea una lista válida)
                val gamesData = document.get("games") as? List<Map<String, Any>> ?: mutableListOf()

                // Busca si el juego ya existe en la lista
                val updatedGames = gamesData.toMutableList()
                val existingGameIndex = updatedGames.indexOfFirst { (it["id"] as? String) == game.id }

                // Prepara el juego para la actualización
                val gameData = mapOf(
                    "id" to game.id,
                    "name" to (game.name ?: ""),  // Si `name` es null, se asigna una cadena vacía
                    "isLiked" to game.isLiked,
                    "background_image" to (game.backgroundImage ?: ""), // Usa una cadena vacía si es null
                    "rating" to (game.rating ?: 0f) // Usa 0f si el rating es null
                )

                if (existingGameIndex != -1) {
                    // Actualiza el juego existente
                    updatedGames[existingGameIndex] = gameData
                } else {
                    // Agrega el juego nuevo a la lista
                    updatedGames.add(gameData)
                }

                // Guarda la lista actualizada de juegos en Firestore
                userLikedGamesRef.update("games", updatedGames)
                    .addOnSuccessListener {
                        println("Estado del juego guardado con éxito.")
                    }
                    .addOnFailureListener { e ->
                        println("Error al guardar el estado del juego: ${e.message}")
                    }
            } else {
                println("El documento no existe o está vacío.")
            }
        }.addOnFailureListener { e ->
            println("Error al obtener la lista de juegos: ${e.message}")
        }
    }







}