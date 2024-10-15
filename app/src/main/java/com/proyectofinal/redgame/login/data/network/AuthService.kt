package com.proyectofinal.redgame.login.data.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.proyectofinal.redgame.login.data.model.UsuarioModel

class AuthService {

    private  var auth: FirebaseAuth= FirebaseAuth.getInstance()
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()


    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    Log.d("AuthService","Usuario registrado : ${user?.uid}")
                    onComplete(true,user?.uid)
                } else{
                    Log.w("AuthService","Registro fallido", task.exception)
                    onComplete(false,null)
                }




            }






    }
    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("AuthService", "Usuario autenticado: ${user?.uid}")
                    onComplete(true, user?.uid) // Éxito
                } else {
                    Log.w("AuthService", "Autenticación fallida", task.exception)
                    onComplete(false, null) // Error
                }
            }
    }









}