package com.proyectofinal.redgame.data.model

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import com.proyectofinal.redgame.R


data class Platform(
    @SerializedName("platform") val platform: PlatformDetails
)

data class PlatformDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class GameModel(
    @SerializedName("id") val id: String = "", // Cambiado a String para Firestore
    @SerializedName("name") val name: String = "",
    @SerializedName("released") val released: String? = null,
    @SerializedName("background_image") val backgroundImage: String? = null,
    @SerializedName("rating") val rating: Float = 0f,
    @SerializedName("platforms") val platforms: List<Platform>? = emptyList(),
    var isLiked: Boolean = false,

    ) {
    companion object {
        fun fromMap(map: Map<String, Any?>): GameModel {

            //implementar lo que esta en comentarios es la plataforma de los juegos
            val platformsList = (map["platforms"] as? List<Map<String, Any>>)
                ?.mapNotNull { platformMap ->
                    val platformDetails = platformMap["platform"] as? Map<String, Any>
                    platformDetails?.let {
                        Platform(
                            platform = PlatformDetails(
                                id = it["id"] as? Int ?: 0,
                                name = it["name"] as? String ?: ""
                            )
                        )
                    }
                } ?: emptyList()


            return GameModel(
                id = map["id"] as? String ?: "", // Manejo de nulos
                name = map["name"] as? String ?: "",
                released = map["released"] as? String,
                backgroundImage = map["background_image"] as? String ?: "", // Cambia a 'background_image'
                rating = (map["rating"] as? Number)?.toFloat() ?: 0f,
                isLiked = map["isLiked"] as? Boolean ?: false,
                platforms = platformsList
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
