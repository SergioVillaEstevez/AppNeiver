package com.proyectofinal.redgame.login.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.proyectofinal.redgame.databinding.ActivityInicioSisionBinding
import com.proyectofinal.redgame.login.data.network.AuthService
import com.proyectofinal.redgame.login.ui.registrar.RegistrarActivity
import com.proyectofinal.redgame.ui.home.MainActivity

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioSisionBinding
    private val auth: AuthService= AuthService()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioSisionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener() {

            iniciarSesion()

        }
        binding.btnIrACrearMiCuenta.setOnClickListener() {
            initIrACrearCuenta()
        }




    }

    fun iniciarSesion(){

       val usuario =  binding.etUsuario.text.toString()
        val password= binding.etPassword.text.toString()

        if (usuario.isEmpty() || password.isEmpty()) {
            Log.d("IniciarActivity", "Error: Campos vacíos")
            Toast.makeText(applicationContext, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        }
        auth.loginUser(usuario,password){success, userId->

            if(success){
                Log.d("IniciarActivity", " inicio sesion correcto")
                 initIntentMain()

            }
            else{

                Log.d("IniciarActivity", "Error usuario o contraseña")
                Toast.makeText(applicationContext, "Usuario o contraseña incorrectos. Inténtelo de nuevo.", Toast.LENGTH_SHORT).show()
            }



        }

    }

    fun initIntentMain() {

        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    fun initIrACrearCuenta() {
        val intent = Intent(this, RegistrarActivity::class.java)
        startActivity(intent)

    }


}