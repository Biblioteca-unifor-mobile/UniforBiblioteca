package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.dataclass.PastaCardData
import com.example.uniforbiblioteca.dialog.DialogCriarPasta
import com.example.uniforbiblioteca.rvadapter.PastaAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PastasFragment : androidx.fragment.app.Fragment() {


    lateinit var addFAB: FloatingActionButton



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(_root_ide_package_.com.example.uniforbiblioteca.R.layout.fragment_pastas, container, false)

        //
        val recyclerView: RecyclerView = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.pastasRecyclerView) // Ajuste o id para o seu XML
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Pastas de placeholders
        val pastas = listOf(
            PastaCardData(
                1,
                "Estrutura de Dados",
                "Ultima Modificação: 2025-10-08",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                2,
                "Java",
                "Ultima Modificação: 2025-10-07",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                3,
                "Redes",
                "Ultima Modificação: 2025-10-06",
                "https://placehold.co/200x300/png"
            ),
            PastaCardData(
                4,
                "Integração",
                "Ultima Modificação: 2025-10-05",
                "https://placehold.co/200x300/png"
            ),
        )
        // Adapter
        val adapter =
            PastaAdapter(pastas) { pasta ->
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.mainFragmentContainer,
                        PastaFragment::class.java,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }

        recyclerView.adapter = adapter

        addFAB = view.findViewById(_root_ide_package_.com.example.uniforbiblioteca.R.id.novaPasta)

        addFAB.setOnClickListener {
            val dialog = DialogCriarPasta()
            dialog.show(parentFragmentManager, "CriarPasta")
        }

        return view
    }



    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("pastas")
    }
}