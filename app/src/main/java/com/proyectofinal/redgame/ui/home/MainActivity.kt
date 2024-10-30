package com.proyectofinal.redgame.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.proyectofinal.redgame.R
import com.proyectofinal.redgame.databinding.ActivityMainBinding
import com.proyectofinal.redgame.login.ui.home.LoginActivity
import com.proyectofinal.redgame.ui.foro.ForoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val foroViewModel: ForoViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.menuAjustes.setOnClickListener(){ view->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_ajustes,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.misDatos-> {
                        // Acción para la opción 1
                        true
                    }
                    R.id.cerrarSesion -> {

                        FirebaseAuth.getInstance().signOut() // Cerrar sesión
                        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

                        // Opcional: Redirigir a la actividad de inicio de sesión
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // Opcional: Termina la actividad actual



                        // Acción para la opción 2
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()


        }


        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {

        initNavigation()
    }
    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.framentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)

        //listener para configurar los clics de la navegacion inferior
        binding.bottomNavView.setOnItemSelectedListener{item->
            when (item.itemId){
                R.id.perfilFragment-> {
                    navController.navigate(R.id.perfilFragment)
                    true
                }
                R.id.juegosFragment -> {
                    navController.navigate(R.id.juegosFragment)
                    true
                }
                R.id.foroFragment -> {
                    navController.navigate(R.id.foroFragment)
                    true
                }

                else -> false
            }
        }

    }


}
