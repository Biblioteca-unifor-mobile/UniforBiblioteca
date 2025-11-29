package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.User
import com.example.uniforbiblioteca.dataclass.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UsersAdapter(
    private val users: List<Usuario>,
    private val onItemClick: (Usuario) -> Unit,
    private val onDeleteClick: suspend (Usuario) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val scope = CoroutineScope(Dispatchers.Main)

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.nome_user_card)

        private val delete: Button = itemView.findViewById(R.id.del_user_card)

        fun bind(user: Usuario) {
            name.text = user.nome
            itemView.setOnClickListener {
                scope.launch {
                    onItemClick(user)
                }
            }

            delete.setOnClickListener {
                scope.launch {
                    onDeleteClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_user_card, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}
