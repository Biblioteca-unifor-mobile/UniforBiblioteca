package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.rvadapter.UsersAdapter
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dataclass.Usuario
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarUser
import com.example.uniforbiblioteca.viewmodel.UsersManager


class AdminUsersFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_users, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.admin_users_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lista de placeholders
        val users = listOf(
            Usuario("1234567", "Joáo")
        )

        // Adapter
        val adapter = UsersAdapter(users, this::getProfile, this::onDeleteClick)

        recyclerView.adapter = adapter


        return view
    }

    suspend fun onDeleteClick(user: Usuario){
        DialogConfirmarDeletarUser
            .newInstance(user.nome) {
                deleteUser(user)
            }
            .show(parentFragmentManager, "confirmarDeletarUser")
    }


    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("users")
    }

    suspend fun deleteUser(user: Usuario){
        try {
            UsersManager.deleteUser(user)
            parentFragmentManager.popBackStack()
        } catch(e: Exception){
            Toast.makeText(requireContext(), "Não foi possivel deletar usuário", Toast.LENGTH_SHORT).show()
        }
    }

    fun getProfile(user: Usuario){

        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, UserProfileFragment::class.java, null)
            .addToBackStack(null)
            .commit()
        return
    }

}