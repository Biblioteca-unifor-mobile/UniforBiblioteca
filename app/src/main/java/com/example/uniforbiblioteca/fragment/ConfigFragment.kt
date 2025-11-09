package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R


class ConfigFragment : Fragment() {


    lateinit var voltar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_config, container, false)

        voltar = view.findViewById(R.id.config_voltar)

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("Config")
    }
}