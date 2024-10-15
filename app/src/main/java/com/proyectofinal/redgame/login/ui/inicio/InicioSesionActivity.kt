package com.proyectofinal.redgame.login.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.util.Log
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


        auth.loginUser(usuario,password){success, userId->

            if(success){
                Log.d("IniciarActivity", " inicio sesion correcto")
                 initIntentMain()

            }
            else{

                Log.d("IniciarActivity", "Error usuario o contraseña")
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