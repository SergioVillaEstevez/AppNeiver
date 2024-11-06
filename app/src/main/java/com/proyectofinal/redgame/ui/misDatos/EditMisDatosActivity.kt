package com.proyectofinal.redgame.ui.misDatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.databinding.ActivityEditMisDatosBinding
import com.proyectofinal.redgame.ui.home.MainActivity

class EditMisDatosActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityEditMisDatosBinding
    private var db= FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditMisDatosBinding.inflate(layoutInflater)

        binding.btnActualizarDatos.setOnClickListener(){

            if(binding.etNombreCompletoUpdate.text.isEmpty()|| binding.etNombreUsuarioUpdate.text.isEmpty()|| binding.etPasswordUpdate.text.isEmpty()||binding.etCorreoElectronicoUpdate.text.isEmpty() ){

                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()


            }
            else{
                updateUserDataBase()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
            }



        }

        binding.btnatrasEditDatos.setOnClickListener(){

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }



    fun updateUserDataBase(){

        val email= binding.etCorreoElectronicoUpdate.text.toString().trim()
        val password= binding.etPasswordUpdate.text.toString().trim()
        val nombre_completo= binding.etNombreCompletoUpdate.text.toString()
        val nombre_usuario= binding.etNombreUsuarioUpdate.text.toString()


        val userData= hashMapOf(
            "nombre_usuario" to nombre_usuario,
            "nombre_completo" to nombre_completo,
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
}