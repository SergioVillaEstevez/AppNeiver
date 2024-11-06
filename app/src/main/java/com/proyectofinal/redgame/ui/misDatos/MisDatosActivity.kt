package com.proyectofinal.redgame.ui.misDatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyectofinal.redgame.databinding.ActivityMisDatosBinding
import com.proyectofinal.redgame.ui.home.MainActivity

class MisDatosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMisDatosBinding

    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMisDatosBinding.inflate(layoutInflater)
        val email= FirebaseAuth.getInstance().currentUser?.email

        if(email!=null){
        cargarDatosUsuario(email)}
        else{
            Log.d("MisDatosActivity", "El usuario no está autenticado")
        }


        binding.btnEditDatos.setOnClickListener(){

            val intent= Intent(this,EditMisDatosActivity::class.java)
            startActivity(intent)



        }


        binding.btnatrasMisDatos.setOnClickListener(){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        setContentView(binding.root)
    }






    fun cargarDatosUsuario(email: String) {
        db.collection("Usuarios").document(email).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombreCompleto = document.getString("nombre_completo")
                    val nombreUsuario = document.getString("nombre_usuario")
                    val correo = document.getString("correo_electronico")
                    val password = document.getString("password")

                    // Verificamos si cada valor no es null y luego lo asignamos al hint correspondiente
                    if (!nombreCompleto.isNullOrEmpty()) {
                        binding.tvNombreCompletoUpdate.text = nombreCompleto
                    }
                    if (!nombreUsuario.isNullOrEmpty()) {
                        binding.tvNombreUsuarioUpdate.text = nombreUsuario
                    }
                    if (!correo.isNullOrEmpty()) {
                        binding.tvCorreoElectronicoUpdate.text = correo
                    }
                    if (!password.isNullOrEmpty()) {
                        binding.tvPasswordUpdate.text = password
                    }
                } else {
                    Log.d("Firestore", "No se encontró el documento")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "Error al obtener el documento", exception)
            }
    }

}