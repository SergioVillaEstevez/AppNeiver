package com.proyectofinal.redgame.data.model

import com.google.gson.annotations.SerializedName

data class GameModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String, // Agregado: nombre del juego
    @SerializedName("released") val released: String,
    @SerializedName("background_image") val backgroundImage: String, // Cambié el nombre a camelCase para que siga las convenciones de Kotlin
    @SerializedName("rating") val rating: Float
)
