package com.proyectofinal.redgame.ui.foro.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClearChatWorker( context: Context,workerParams: WorkerParameters): CoroutineWorker(context,workerParams) {

    override suspend fun doWork(): Result {
        Log.d("ClearChatWorker", "Ejecutando ClearChatWorker a las: ${System.currentTimeMillis()}")

        val db = FirebaseFirestore.getInstance()
        val chatCollection = db.collection("Posts")

        return try {
            // Obtener y borrar los documentos en la colección "Posts"
            val snapshot = chatCollection.get().await() // Espera la finalización de la tarea

            // Verifica si hay documentos para borrar
            if (snapshot.isEmpty) {
                Log.d("ClearChatWorker", "No hay documentos para borrar.")
            } else {
                for (document in snapshot.documents) {
                    document.reference.delete().await() // Espera que se elimine cada documento
                }
                Log.d("ClearChatWorker", "Chat borrado exitosamente.")
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("ClearChatWorker", "Error en ClearChatWorker: ${e.message}")
            Result.failure()
        }
    }
}