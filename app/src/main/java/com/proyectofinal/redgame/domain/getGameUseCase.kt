package com.proyectofinal.redgame.domain

import com.proyectofinal.redgame.data.GameRepository
import com.proyectofinal.redgame.data.model.GameModel

class getGameUseCase {

    private val repository= GameRepository()

    suspend operator fun invoke(): List<GameModel>? = repository.getAllGames()


}