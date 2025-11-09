package com.example.uniforbiblioteca.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.User

class UsersAdapter(
    private val users: List<User>,
    private val onItemClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.nome_user_card)

        private val delete: Button = itemView.findViewById(R.id.del_user_card)

        fun bind(user: User) {
            name.text = user.name
            itemView.setOnClickListener { onItemClick(user) }
            delete.setOnClickListener { onDeleteClick(user) }
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
