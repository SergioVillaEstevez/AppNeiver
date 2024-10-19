package com.proyectofinal.redgame.ui.juegosGuardados

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JuegosGuardadosViewModel @Inject constructor() : ViewModel() {

    private var _likedGame = MutableStateFlow<List<GameModel>>(emptyList())
    val likedGame: StateFlow<List<GameModel>> = _likedGame


    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

     fun fetchLikedGame(gameViewModel: GameViewModel) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            db.collection("JuegosGuardados").document(userId ?: "default_user")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {

                        val gameData = document.data?.get("games") as List<Map<String, Any>>
                        val likeGame = gameData.map { GameModel.fromMap(it) }
                        _likedGame.value = likeGame



                        likeGame.forEach { likeGame ->

                            gameViewModel.getGameList()
                                .find { it.id == likeGame.id }?.isLiked = likeGame.isLiked
                        }
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


    fun removeLikedGame(game:GameModel){

        val currentGame = _likedGame.value.toMutableList()

        if(currentGame.contains(game)){

            currentGame.remove(game)
            _likedGame.value= currentGame
        }

        val userLinkedGameRef =
            db.collection("JuegosGuardados").document(userId?:"defaul_user")
        userLinkedGameRef.set(mapOf("game" to currentGame.map {game ->
            mapOf(
                "id" to game.id,
                "name" to game.name,
                "isLiked" to game.isLiked,
                "background_image" to game.backgroundImage,
                "rating" to game.rating
            )

        }))
            .addOnSuccessListener {
                println("Lista actualizada guardada con éxito.")
            }
            .addOnFailureListener { e ->
                println("Error al guardar la lista actualizada: ${e.message}")
            }





    }

}









