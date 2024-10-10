package com.proyectofinal.redgame.login.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyectofinal.redgame.databinding.ActivityLoginBinding
import com.proyectofinal.redgame.login.ui.inicio.InicioSesionActivity
import com.proyectofinal.redgame.login.ui.registrar.RegistrarActivity
import com.proyectofinal.redgame.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.btnRegistrar.setOnClickListener() {
            initIntetRegistrar()
        }

        binding.btnIniciarSesion.setOnClickListener {

            initIntetInicioSesion()

        }


    }


    private fun initIntetInicioSesion() {
        val intent = Intent(this, InicioSesionActivity::class.java)
        startActivity(intent)
    }

    private fun initIntetRegistrar() {
        val intent = Intent(this, RegistrarActivity::class.java)
        startActivity(intent)

    }
}