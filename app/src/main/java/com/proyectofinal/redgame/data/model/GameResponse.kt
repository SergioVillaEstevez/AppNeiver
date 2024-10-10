package com.proyectofinal.redgame.data.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,

    @SerializedName("results") val results: List<GameModel>




)
