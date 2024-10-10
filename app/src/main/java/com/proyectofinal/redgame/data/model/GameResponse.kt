package com.proyectofinal.redgame.data.model

import com.google.gson.annotations.SerializedName

data class GameResponse(  @SerializedName("results") val results: List<GameModel>)
