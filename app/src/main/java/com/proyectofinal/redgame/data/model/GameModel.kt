package com.proyectofinal.redgame.data.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import com.proyectofinal.redgame.R

data class GameModel(
    @SerializedName("id") val id: String = "", // Cambiado a String para Firestore
    @SerializedName("name") val name: String = "",
    @SerializedName("released") val released: String = "",
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("rating") val rating: Float = 0f,
    var isLiked: Boolean = false
) {
    companion object {
        fun fromMap(map: Map<String, Any?>): GameModel {
            return GameModel(
                id = map["id"] as? String ?: "", // Manejo de nulos
                name = map["name"] as? String ?: "",
                released = map["released"] as? String ?: "",
                backgroundImage = map["background_image"] as? String ?: "", // Cambia a 'background_image'
                rating = (map["rating"] as? Number)?.toFloat() ?: 0f,
                isLiked = map["isLiked"] as? Boolean ?: false
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameModel) return false
        return id == other.id // Compara por ID
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
