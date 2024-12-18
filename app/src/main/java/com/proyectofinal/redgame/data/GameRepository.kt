package com.proyectofinal.redgame.data

import com.proyectofinal.redgame.data.model.GameModel
import com.proyectofinal.redgame.data.model.GameProvider
import com.proyectofinal.redgame.data.network.GameService

class GameRepository {

    private val api = GameService()

    suspend fun getAllGames(search:String=""): List<GameModel> {


        val response = api.getGames(page = 1, pageSize = 10, search = "", ordering = "rating")

        GameProvider.games = response

        return response


    }
}