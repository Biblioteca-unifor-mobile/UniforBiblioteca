package com.example.uniforbiblioteca.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.AdminActivity

class AdminHomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    lateinit var sairBtn: Button
    lateinit var nomeLbl: TextView
    lateinit var cargoLbl: TextView
    lateinit var greetingLbl: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sairBtn = view.findViewById(R.id.admin_sair)
        nomeLbl = view.findViewById(R.id.user_nome)
        cargoLbl = view.findViewById(R.id.user_cargo)
        greetingLbl = view.findViewById(R.id.user_greeting)

        nomeLbl.text = "Narak"
        cargoLbl.text = "Desenvolvedor"
        greetingLbl.text = "Seja bem vindo(a), " + nomeLbl.text

        sairBtn.setOnClickListener {
            (activity as? AdminActivity)?.sair()
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.changeState("home")
    }
}