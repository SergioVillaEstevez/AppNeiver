package com.proyectofinal.redgame.login.ui.inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.databinding.ActivityInicioSisionBinding
import com.proyectofinal.redgame.login.ui.registrar.RegistrarActivity
import com.proyectofinal.redgame.ui.home.MainActivity

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioSisionBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding= ActivityInicioSisionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener(){
            initIntentMain()
        }
        binding.btnIrACrearMiCuenta.setOnClickListener(){
            initIrACrearCuenta()
        }



    }

    fun initIntentMain(){

        var intent= Intent(this,MainActivity::class.java)
        startActivity(intent)

    }

    fun initIrACrearCuenta(){
        val intent= Intent(this, RegistrarActivity::class.java)
        startActivity(intent)

    }


}