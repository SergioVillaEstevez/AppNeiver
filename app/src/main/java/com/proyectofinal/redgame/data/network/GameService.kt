package com.proyectofinal.redgame.data.network

import android.util.Log
import com.proyectofinal.redgame.core.RetrofitHelper
import com.proyectofinal.redgame.data.model.GameModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GameService {
    private val retrofit = RetrofitHelper.getRetrofit()


    suspend fun getGames(): List<GameModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofit.create(GameApiClient::class.java)
                    .getAllGames("ea5710fd888a4d6b82220d407aa759e8", page = 1, pageSize = 40)

                Log.d("GameService", "Response: ${response.body()}")
                if (response.isSuccessful) {
                    // Accedemos a la lista de juegos dentro de GameResponse
                    response.body()?.results ?: emptyList()
                } else {
                    // Manejo del error si la respuesta no es exitosa
                    emptyList()
                }
            } catch (e: HttpException) {
                // Manejo de excepciones específicas de Retrofit
                e.printStackTrace()
                emptyList()
            } catch (e: Exception) {
                // Manejo de otras excepciones
                e.printStackTrace()
                emptyList()
            }
        }

    }


}
