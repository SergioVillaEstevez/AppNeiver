package com.proyectofinal.redgame.data.network

import com.proyectofinal.redgame.data.model.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiClient {
    @GET("games")
    suspend fun getAllGames(

        @Query("key") apiKey: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String = ""




    ): Response<GameResponse>


}