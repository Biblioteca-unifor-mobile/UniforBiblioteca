package com.example.uniforbiblioteca.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.viewmodel.FontManager
import com.example.uniforbiblioteca.viewmodel.AccessibilityManager

class ConfigFragment : Fragment() {

    lateinit var voltar: Button
    lateinit var aumentarBtn: ImageButton
    lateinit var diminuirBtn: ImageButton

    lateinit var dislexiaSwitch: Switch
    lateinit var daltonicoSwitch: Switch


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_config, container, false)

        voltar = view.findViewById(R.id.config_voltar)
        aumentarBtn = view.findViewById(R.id.maiorBtn)
        diminuirBtn = view.findViewById(R.id.menorBtn)
        dislexiaSwitch = view.findViewById(R.id.dislexiaSwitch)
        daltonicoSwitch = view.findViewById(R.id.daltonicoSwitch)

        dislexiaSwitch.isChecked =
            AccessibilityManager.isDyslexiaEnabled(requireContext())

        daltonicoSwitch.isChecked =
            AccessibilityManager.isDaltonicEnabled(requireContext())

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        aumentarBtn.setOnClickListener {
            FontManager.increase(requireContext())
            requireActivity().recreate()
        }

        diminuirBtn.setOnClickListener {
            FontManager.decrease(requireContext())
            requireActivity().recreate()
        }

        dislexiaSwitch.setOnCheckedChangeListener { _, isChecked ->
            AccessibilityManager.setDyslexia(requireContext(), isChecked)
            requireActivity().recreate()
            applyAccessibility(view, requireContext())
        }

        daltonicoSwitch.setOnCheckedChangeListener { _, isChecked ->
            AccessibilityManager.setDaltonic(requireContext(), isChecked)
            requireActivity().recreate()
        }
        applyAccessibility(view, requireContext())
        return view
    }

    fun applyAccessibility(root: View, context: Context) {
        if (root is TextView) {
            val typeface = if (dislexiaSwitch.isChecked) {
                ResourcesCompat.getFont(context, R.font.opendyslexic)
            } else {
                null // null = volta para fonte padrão do XML
            }
            root.typeface = typeface
        }

        if (AccessibilityManager.isDaltonicEnabled(context)) {
            root.setBackgroundColor(Color.WHITE)
        } else {
            // opcional: restaurar cor padrão
            root.setBackgroundColor(Color.TRANSPARENT)
        }

        if (root is ViewGroup) {
            for (i in 0 until root.childCount) {
                applyAccessibility(root.getChildAt(i), context)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("Config")
    }
}