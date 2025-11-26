package com.example.uniforbiblioteca.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.dialog.HistoricoFilterDialogFragment
import com.example.uniforbiblioteca.rvadapter.HistoricoAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoricoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_historico, container, false)

        // RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHistorico) // Ajuste o id para o seu XML
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lista de placeholders
        val livros = listOf(
            LivroCardData(
                "1",
                "Livro 1",
                "Autor 1",
                "5 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "2",
                "Livro 2",
                "Autor 2",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "3",
                "Livro 3",
                "Autor 3",
                "8 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "4",
                "Livro 4",
                "Autor 4",
                "30 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "5",
                "Livro 5",
                "Autor 5",
                "50 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "6",
                "Livro 6",
                "Autor 6",
                "50 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "7",
                "Livro 7",
                "Autor 7",
                "70 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "8",
                "Livro 8",
                "Autor 8",
                "90 dias atrás",
                "https://placehold.co/200x300/png"
            ),
            LivroCardData(
                "9",
                "Livro 9",
                "Autor 9",
                "90 dias atrás",
                "https://placehold.co/200x300/png"
            )
        )

        // Adapter
        val adapter = HistoricoAdapter(livros) { livro ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, LivroFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFiltro = view.findViewById<Button>(R.id.historicoFiltroBtn)

        btnFiltro.setOnClickListener {
            val dialog = HistoricoFilterDialogFragment()
            dialog.show(parentFragmentManager, "HistoricoFiltroDialog")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoricoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoricoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("historico")
    }
}