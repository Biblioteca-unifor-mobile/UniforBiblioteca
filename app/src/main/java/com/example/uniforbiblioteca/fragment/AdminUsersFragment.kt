package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch


class AdminUsersFragment : Fragment() {
    lateinit var adapter: UsersAdapter

    var users = mutableListOf<Usuario>()
    var display = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_users, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.admin_users_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Adapter
        adapter = UsersAdapter(mutableListOf(), this::getProfile, this::onDeleteClick)

        recyclerView.adapter = adapter

        lifecycleScope.launch {
            UsersManager.getUserList()
            adapter.updateData(UsersManager.userList)
        }

        return view
    }

    fun onDeleteClick(user: Usuario){
        DialogConfirmarDeletarUser
            .newInstance(user.nome) {
                try {
                    deleteUser(user)
                    Toast.makeText(requireContext(), "User deletado com sucesso", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("DELETE_USER", e.message.toString())
                    Toast.makeText(requireContext(), "Náo foi possivel deletar user", Toast.LENGTH_SHORT).show()
                }
            }
            .show(parentFragmentManager, "confirmarDeletarUser")
    }

    fun filter(parcial: String){
        return
    }


    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("users")
        lifecycleScope.launch {
            UsersManager.getUserList()
            adapter.updateData(UsersManager.userList)
        }
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
            .replace(R.id.adminFragmentContainer, UserProfileFragment(user))
            .addToBackStack(null)
            .commit()
        return
    }

}