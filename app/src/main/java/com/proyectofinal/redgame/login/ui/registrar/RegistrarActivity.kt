package com.proyectofinal.redgame.login.ui.registrar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

import com.proyectofinal.redgame.databinding.ActivityRegistrarBinding
import com.proyectofinal.redgame.login.data.network.AuthService
import com.proyectofinal.redgame.login.ui.inicio.InicioSesionActivity
import com.proyectofinal.redgame.ui.home.MainActivity

class RegistrarActivity : AppCompatActivity() {

    private val authService = AuthService()
    private var db:FirebaseFirestore= FirebaseFirestore.getInstance()


    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRegistrarBinding.inflate(layoutInflater)


        setContentView(binding.root)

        val email= binding.etCorreoElectronico.text.toString().trim()
        val password= binding.etNuevaPassword.text.toString().trim()




        binding.btnIrIniciarSesion.setOnClickListener() {
            initIntentIrInicarSesion()
        }
        binding.btnCrearMiCuenta.setOnClickListener(){

            registerNewUser()
            initEntrar()



        }





    }

    fun registerNewUser(){


        val email= binding.etCorreoElectronico.text.toString().trim()
        val password= binding.etNuevaPassword.text.toString().trim()
        val nombre_completo= binding.etNombreCompleto.text.toString()
        val nombre_usuario= binding.etNuevoUsuario.text.toString()
        authService.registerUser(email,password){success, userId->


            if (success) {
                // Registro exitoso, guardar información adicional en la base de datos
                Log.d("RegisterActivity", "Registro realizado con exito")
                saveUserToDatabase()
                //creo esta funcion para guardar los datos en la base de datos que tengo que creear


            } else {
                // Manejo de errores si el registro falla
                Log.d("RegisterActivity", "Error registrando usuario")


            }







        }

    }


    fun saveUserToDatabase(){
        val email= binding.etCorreoElectronico.text.toString().trim()
        val password= binding.etNuevaPassword.text.toString().trim()
        val nombre_completo= binding.etNombreCompleto.text.toString()
        val nombre_usuario= binding.etNuevoUsuario.text.toString()

        if (nombre_completo.isEmpty() || nombre_usuario.isEmpty() || email.isEmpty()||password.isEmpty()) {
            Log.d("RegisterActivity", "Faltan datos para guardar en la base de datos")
            return
        }

        val userData= hashMapOf(
            "nombre_completo" to nombre_completo,
            "nombre_usuario" to nombre_usuario,
            "correo_electronico" to email,
            "password" to password

        )

        db.collection("Usuarios").document(email).set(userData)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Datos del usuario guardados con éxito")
            }
            .addOnFailureListener { e ->
                Log.d("RegisterActivity", "Error al guardar datos: ${e.message}")
            }



    }

    fun initIntentIrInicarSesion() {

        var intent = Intent(this, InicioSesionActivity::class.java)

        startActivity(intent)

    }
    fun initEntrar() {

        var intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

    }

}