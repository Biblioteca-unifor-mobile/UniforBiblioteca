package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.uniforbiblioteca.R
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.AdminActivity
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dataclass.Usuario
import com.example.uniforbiblioteca.rvadapter.AcervoAdapter
import com.example.uniforbiblioteca.rvadapter.HistoricoAdapter
import com.example.uniforbiblioteca.rvadapter.PastaAdapter
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarUser
import com.example.uniforbiblioteca.viewmodel.UsersManager
import kotlinx.coroutines.launch

class UserProfileFragment(var user: Usuario) : androidx.fragment.app.Fragment() {


    // TextViews
    private lateinit var userName: TextView
    private lateinit var userMatricula: TextView
    private lateinit var userEmail: TextView
    private lateinit var lblEmprestimos: TextView
    private lateinit var lblReservas: TextView
    private lateinit var lblListas: TextView

    // Buttons
    private lateinit var cestaBtn: Button
    private lateinit var deleteBtn: Button


    // RecyclerViews
    private lateinit var rvEmprestimos: RecyclerView
    private lateinit var rvReservas: RecyclerView
    private lateinit var rvListas: RecyclerView
    private lateinit var adapterEmprestimo: HistoricoAdapter
    private lateinit var adapterReserva: AcervoAdapter
    private lateinit var adapterListas: PastaAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        userName = view.findViewById(R.id.user_profile_name)
        userMatricula = view.findViewById(R.id.user_profile_matricula)
        userEmail = view.findViewById(R.id.user_profile_email)
        lblEmprestimos = view.findViewById(R.id.lbl_user_profile_emprestimos)
        lblReservas = view.findViewById(R.id.lbl_user_profile_reservas)
        lblListas = view.findViewById(R.id.lbl_user_profile_listas)

        cestaBtn = view.findViewById(R.id.user_profile_cesta_button)
        deleteBtn = view.findViewById(R.id.user_profile_delete_button)

        rvEmprestimos = view.findViewById(R.id.user_profile_emprestimos_rv)
        rvReservas = view.findViewById(R.id.user_profile_reservas_rv)
        rvListas = view.findViewById(R.id._user_profile_listas_rv)

        rvEmprestimos.layoutManager = LinearLayoutManager(requireContext())
        rvReservas.layoutManager = LinearLayoutManager(requireContext())
        rvListas.layoutManager = LinearLayoutManager(requireContext())


        // Adapter
        adapterEmprestimo = HistoricoAdapter(UsersManager.selectedUserEmprestimos) { livro ->
        }

        rvEmprestimos.adapter = adapterEmprestimo

        // Adapter
        var livrosReservados: MutableList<LivroData> = mutableListOf()
        for (reserva in UsersManager.selectedUserReservation){
            if (reserva.bookCopy == null) continue
            if (reserva.bookCopy.book == null) continue
            livrosReservados.add(reserva.bookCopy.book)
        }
        adapterReserva = AcervoAdapter(livrosReservados) { livro ->
        }

        rvReservas.adapter = adapterReserva
        // Adapter
        adapterListas =
            PastaAdapter(UsersManager.selectedUserFolders) { pasta ->
            }

        rvListas.adapter = adapterListas

        cestaBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminCestaFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        deleteBtn.setOnClickListener {
            DialogConfirmarDeletarUser
                .newInstance(userName.text.toString()){
                    deleteUser(UsersManager.selectedUser!!)
                }
                .show(parentFragmentManager, "confirmarDeletarUser")
        }

        userMatricula.text = "Carregando perfil..."
        userName.text = ""
        userEmail.text = ""

        return view
    }

    suspend fun deleteUser(user: Usuario){
        try {
            UsersManager.deleteUser(user)
            parentFragmentManager.popBackStack()
        } catch(e: Exception){
            Toast.makeText(requireContext(), "Não foi possivel deletar usuário", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getProfile(){
        if (UsersManager.selectUser(user)) {
            userMatricula.text = UsersManager.selectedUser?.matricula
            userName.text = UsersManager.selectedUser?.nome
            userEmail.text = UsersManager.selectedUser?.email
            adapterListas.updateItems(UsersManager.selectedUserFolders)
            var livrosReservados: MutableList<LivroData> = mutableListOf()
            for (reserva in UsersManager.selectedUserReservation){
                if (reserva.bookCopy == null) continue
                if (reserva.bookCopy.book == null) continue
                livrosReservados.add(reserva.bookCopy.book)
            }
            adapterReserva.updateData(livrosReservados)
            var livrosEmprestados: MutableList<LivroData> = mutableListOf()
            for (reserva in UsersManager.selectedUserReservation){
                if (reserva.bookCopy == null) continue
                if (reserva.bookCopy.book == null) continue
                livrosReservados.add(reserva.bookCopy.book)
            }
            adapterEmprestimo.updateItems(UsersManager.selectedUserEmprestimos)
            return
        }
        parentFragmentManager.popBackStack()
    }

    override fun onStop() {

        super.onStop()
        UsersManager.reset()
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("User Profile")

        lifecycleScope.launch {
            getProfile()
        }
    }
}
