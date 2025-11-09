package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.fragment.AcervoFragment
import com.example.uniforbiblioteca.fragment.ChatFragment
import com.example.uniforbiblioteca.fragment.ConfigFragment
import com.example.uniforbiblioteca.fragment.ContatoFragment


class MenuFragment : androidx.fragment.app.Fragment() {


    lateinit var contatoBtn: TextView
    lateinit var chatBtn: TextView
    lateinit var configBtn: TextView
    lateinit var acervoBtn: TextView
    lateinit var voltar: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(_root_ide_package_.com.example.uniforbiblioteca.R.layout.fragment_menu, container, false)
    }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("menu")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contatoBtn = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.menuContatoBtn)
        chatBtn = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.menuChatBtn)
        configBtn = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.menuConfigButton)
        acervoBtn = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.menuAcervoButton)
        voltar = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.voltar_menu)


        chatBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(_root_ide_package_.com.example.uniforbiblioteca.R.id.mainFragmentContainer, ChatFragment::class.java, null)
                .addToBackStack("menu")
                .commit()
        }

        contatoBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(_root_ide_package_.com.example.uniforbiblioteca.R.id.mainFragmentContainer, ContatoFragment::class.java, null)
                .addToBackStack("menu")
                .commit()
        }

        configBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(_root_ide_package_.com.example.uniforbiblioteca.R.id.mainFragmentContainer, ConfigFragment::class.java, null)
                .addToBackStack("menu")
                .commit()
        }

        acervoBtn.setOnClickListener {
            (activity as? MainActivity)?.changeState("acervo")
            parentFragmentManager.beginTransaction()
                .replace(_root_ide_package_.com.example.uniforbiblioteca.R.id.mainFragmentContainer, AcervoFragment::class.java, null)
                .addToBackStack("menu")
                .commit()
        }

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


}