package com.proyectofinal.redgame.login.ui.registrar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.databinding.ActivityRegistrarBinding
import com.proyectofinal.redgame.login.ui.inicio.InicioSesionActivity

class RegistrarActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRegistrarBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.btnIrIniciarSesion.setOnClickListener(){
            initIntentIrInicarSesion()
        }


    }


     fun initIntentIrInicarSesion(){
         var intent= Intent(this, InicioSesionActivity::class.java)
         startActivity(intent)

     }
}