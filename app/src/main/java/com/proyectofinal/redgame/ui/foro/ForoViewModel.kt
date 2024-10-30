package com.proyectofinal.redgame.ui.foro

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.proyectofinal.redgame.data.model.PostModel
import com.proyectofinal.redgame.ui.foro.workers.ClearChatWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
@HiltViewModel
class ForoViewModel @Inject constructor(private val application: Application) : ViewModel() {

    private val workManager = WorkManager.getInstance()

    private var _post = MutableStateFlow<List<PostModel>>(emptyList())
    val post: StateFlow<List<PostModel>> = _post

    private val db = FirebaseFirestore.getInstance()
    private val userID = FirebaseAuth.getInstance().currentUser
    private val userEmail = userID?.email

    // Verificación del correo electrónico antes de cualquier operación
    private fun isUserEmailValid(): Boolean {
        return userEmail != null
    }

    fun savePost(post: PostModel) {
        if (!isUserEmailValid()) {
            Log.e("ForoViewModel", "Error: El correo electrónico del usuario es nulo.")
            return
        }

        Log.d("ForoViewModel", "User Email: $userEmail")

        // Referencia al documento de usuario utilizando el correo como ID
        val userDocRef = db.collection("Usuarios").document(userEmail!!)

        // Obtén el nombre de usuario desde la colección Usuarios
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("nombre_usuario") ?: "Unknown"

                    // Guardar la publicación en una colección general "Posts"
                    val postsRef = db.collection("Posts")

                    postsRef.add(
                        mapOf(
                            "username" to username,
                            "content" to post.content,
                            "timestamp" to post.timestamp,
                            "userEmail" to userEmail // Guardar el correo del autor
                        )
                    )
                        .addOnSuccessListener {
                            Log.d("ForoViewModel", "Publicación guardada con éxito.")
                            fetchPostAll() // Actualizar el RecyclerView después de guardar
                        }
                        .addOnFailureListener { e ->
                            Log.e("ForoViewModel", "Error al guardar la publicación: ${e.message}")
                        }
                } else {
                    Log.e("ForoViewModel", "Error: No se encontró el documento del usuario.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("ForoViewModel", "Error al obtener el nombre de usuario: ${e.message}")
            }
    }

    fun fetchPostAll() {
        if (!isUserEmailValid()) {
            Log.e("ForoViewModel", "Error: El correo electrónico del usuario es nulo.")
            return
        }

        // Usar un listener para obtener actualizaciones en tiempo real
        db.collection("Posts")
            .orderBy(
                "timestamp",
                Query.Direction.DESCENDING
            ) // Ordenar las publicaciones por tiempo descendente
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("ForoViewModel", "Error al obtener las publicaciones: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val postList = snapshots.map { PostModel.fromMap(it.data) }
                    _post.value = postList // Actualiza la lista de publicaciones
                }
            }
    }

    fun ClearChat(context: Context){

        val workManager= WorkManager.getInstance(application)
        val clearChatWorkRequest = PeriodicWorkRequestBuilder<ClearChatWorker>(
            12,TimeUnit.HOURS
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "clearChatWork",
            ExistingPeriodicWorkPolicy.KEEP,
            clearChatWorkRequest
        )


    }
}



