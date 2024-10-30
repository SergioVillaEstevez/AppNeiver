package com.proyectofinal.redgame.ui.foro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.proyectofinal.redgame.data.model.PostModel
import com.proyectofinal.redgame.databinding.FragmentForoBinding
import com.proyectofinal.redgame.ui.foro.adapter.ForoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForoFragment : Fragment() {

    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!

    private val foroViewModel: ForoViewModel by viewModels()
    private lateinit var foroAdapter: ForoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForoBinding.inflate(layoutInflater, container, false)

        binding.buttonPost.setOnClickListener() {

            val postContent = binding.editTextPost.text.toString()

            if (postContent.isNotBlank()) {
                val post = PostModel(
                    userName = "NombreDeUsuario",
                    content = postContent,
                    timestamp = System.currentTimeMillis(),
                    userEmail = "correoElectronico"
                )


                foroViewModel.savePost(post)
                binding.editTextPost.text.clear()

            } else {
                Toast.makeText(context, "No puedes enviar un post vacío", Toast.LENGTH_SHORT).show()
            }

        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerPost()

        foroViewModel.fetchPostAll()
        initRecyclerView()
        foroViewModel.ClearChat(requireContext())


    }

    fun observerPost() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                foroViewModel.post.collect() { post ->

                    foroAdapter.updateListPost(post)


                }


            }
        }


    }


    fun initRecyclerView() {

        val  currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        foroAdapter = ForoAdapter(mutableListOf(), currentUserEmail)



        binding.recyclerViewFeed.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = foroAdapter

        }


    }


}