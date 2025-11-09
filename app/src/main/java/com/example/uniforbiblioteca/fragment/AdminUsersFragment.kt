package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.User
import com.example.uniforbiblioteca.rvadapter.UsersAdapter
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarUser


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
            User(1, "User 1"),
            User(2, "User 2"),
            User(3, "User 3"),
            User(4, "User 4"),
            User(5, "User 5"),
            User(6, "User 6"),
            User(7, "User 7"),
            User(8, "User 8"),
            User(9, "User 9")
        )

        // Adapter
        val adapter = UsersAdapter(users, this::onItemClick, this::onDeleteClick)

        recyclerView.adapter = adapter


        return view
    }

    fun onItemClick(user: User){
        parentFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, UserProfileFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }

    fun onDeleteClick(user: User){
        DialogConfirmarDeletarUser
            .newInstance(user.name)
            .show(parentFragmentManager, "confirmarDeletarUser")
    }


    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("users")
    }

}