package com.proyectofinal.redgame.ui.perfil

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.ListaUsuariosModel
import com.proyectofinal.redgame.data.network.GameService
import com.proyectofinal.redgame.login.data.model.UsuarioModel
import com.proyectofinal.redgame.ui.juegos.CompartirViewModel
import com.proyectofinal.redgame.ui.juegos.GameViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(

    private var gameService : GameService
) : ViewModel() {

    private lateinit var compartirViewModel: CompartirViewModel


    private var _likedGame = MutableStateFlow<List<GameModel>>(emptyList())
    val likedGame: StateFlow<List<GameModel>> = _likedGame

    private var _topValoracionJuego = MutableStateFlow<List<GameModel>>(emptyList())
    val topValoracionJuego: StateFlow<List<GameModel>> = _topValoracionJuego

    private var _listaUsuarios = MutableStateFlow<List<ListaUsuariosModel>>(emptyList())
    val listaUsuarios: StateFlow<List<ListaUsuariosModel>> = _listaUsuarios



    //private var gameService = GameService()

    private val db = FirebaseFirestore.getInstance()
    private val userId=FirebaseAuth.getInstance().currentUser?.email




    // Función para eliminar un juego de la lista de "me gusta" y actualizar Firestore
    fun removeLikedGame(game: GameModel) {
        viewModelScope.launch {
            val currentList = _likedGame.value.toMutableList()

            // Verificar si el juego está en la lista y eliminarlo
            val gameToRemove = currentList.find { it.id == game.id }
            if (gameToRemove != null) {
                // Establecer el estado de "isLiked" a false para la base de datos
                gameToRemove.isLiked = false

                // Eliminarlo de la lista local
                currentList.remove(gameToRemove)

                // Actualizar la lista local
                _likedGame.value = currentList

                // Guardar en Firestore
                saveLikedGamesToFirestore(currentList)



            }
        }
    }



    // Función para guardar la lista de juegos "gustados" en Firestore
    private fun saveLikedGamesToFirestore(games: List<GameModel>) {
        if (userId != null) {
            val userLikedGamesRef = db.collection("JuegosGuardados").document(userId)
            // Usa userLikedGamesRef para trabajar con los datos del usuario actual


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
        } else {
            // Maneja el caso en el que no haya un usuario autenticado
            Log.e("Error", "Usuario no autenticado")
        }
    }

    fun fetchLikedGames(gameViewModel: GameViewModel) {
        viewModelScope.launch {
            db.collection("JuegosGuardados").document(userId?: " default_user")
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
    fun fetchListaUsuarios() {
        viewModelScope.launch {
            try {
                // Recupera todos los documentos de la colección "Usuarios"
                val snapshot = db.collection("Usuarios").get().await()

                // Mapea los documentos a una lista de objetos UsuarioModel
                val usuarios = snapshot.documents.mapNotNull { document ->
                    val nombreUsuario = document.getString("nombre_usuario")
                    val nombreCompleto = document.getString("nombre_completo")



                    // Asegúrate de que todos los campos estén disponibles
                    if (nombreUsuario != null && nombreCompleto != null ) {
                        ListaUsuariosModel(nombreUsuario, nombreCompleto)
                    } else {
                        null
                    }
                }

                // Asigna la lista de usuarios al StateFlow
                _listaUsuarios.value = usuarios
            } catch (e: Exception) {
                Log.e("PerfilViewModel", "Error al obtener usuarios: ${e.message}")
                _listaUsuarios.value = emptyList() // En caso de error, dejamos la lista vacía
            }
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





