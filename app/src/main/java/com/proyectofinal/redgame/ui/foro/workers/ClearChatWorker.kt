package com.proyectofinal.redgame.ui.foro.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClearChatWorker( context: Context,workerParams: WorkerParameters): CoroutineWorker(context,workerParams){

    override suspend fun doWork(): Result {
        val db = FirebaseFirestore.getInstance()
        val chatCollection = db.collection("Posts")

        return try {
            val snapshot = chatCollection.get().await() // Espera a obtener el snapshot
            for (document in snapshot) {
                document.reference.delete().await() // Espera a eliminar cada documento
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace() // Imprime el error para depuración
            Result.failure() // Retorna failure si algo sale mal
        }
    }

}