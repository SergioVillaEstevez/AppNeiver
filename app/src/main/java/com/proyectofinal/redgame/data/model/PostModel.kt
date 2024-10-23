package com.proyectofinal.redgame.data.model

data class PostModel(
    val userName: String,
    val content: String,
    val timestamp: Long,
    val userEmail: String


) {
    companion object {
        fun fromMap(map: Map<String, Any?>): PostModel {
            return PostModel(
                userName = map["username"] as? String ?: "", // Manejo de nulos
                content = map["content"] as? String ?: "",
                timestamp = (map["timestamp"] as? Number)?.toLong() ?: 0L,
                userEmail = map["userEmail"] as? String ?: ""


            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostModel) return false
        return  userName  == other.userName // Compara por ID
    }

    override fun hashCode(): Int {
        return userName.hashCode()
    }
}