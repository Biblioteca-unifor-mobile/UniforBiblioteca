package com.example.uniforbiblioteca.fragment

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.uniforbiblioteca.R
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dataclass.LivroCardData
import com.example.uniforbiblioteca.dataclass.LivroData
import com.example.uniforbiblioteca.dialog.DialogEditarPasta
import com.example.uniforbiblioteca.dialog.NovoMembroPastaDialog
import com.example.uniforbiblioteca.fragment.AcervoFragment
import com.example.uniforbiblioteca.fragment.LivroFragment
import com.example.uniforbiblioteca.rvadapter.AcervoAdapter
import com.example.uniforbiblioteca.ui.DialogConfirmarDeletarPasta
import com.example.uniforbiblioteca.viewmodel.FolderManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PastaFragment : Fragment() {

    private lateinit var pasta: Folder

    private lateinit var pastaFab: FloatingActionButton
    private lateinit var pastaVoltar: Button
    private lateinit var pastaTitulo: TextView
    private lateinit var pastaUltimaModificacao: TextView
    private lateinit var pastaEditBtn: Button
    private lateinit var pastaMembrosBtn: Button
    private lateinit var pastaDeletarBtn: Button
    private lateinit var pastaRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pasta = requireArguments().getSerializable(ARG_PASTA) as Folder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pasta, container, false)

        pastaFab = view.findViewById(R.id.pasta_fab)
        pastaVoltar = view.findViewById(R.id.pasta_voltar)
        pastaTitulo = view.findViewById(R.id.pasta_titulo)
        pastaUltimaModificacao = view.findViewById(R.id.pasta_ultima_modificacao)
        pastaEditBtn = view.findViewById(R.id.pasta_edit_btn)
        pastaMembrosBtn = view.findViewById(R.id.pasta_membros_btn)
        pastaDeletarBtn = view.findViewById(R.id.pasta_deletar_btn)
        pastaRv = view.findViewById(R.id.pasta_rv)

//        val userCanEdit = FolderManager.checkUserFolderRole(requireContext(), pasta, FolderManager.ROLE_EDITOR)
//        if (!userCanEdit) {
//            pastaEditBtn.visibility = View.GONE
//            pastaFab.visibility = View.GONE
//        }

        // Preenche dados da pasta
        pastaTitulo.text = pasta.nome
        pastaUltimaModificacao.text = pasta.updatedAt

        // Converte livros da pasta em LivroCardData
        val livros = pasta.books ?: mutableListOf()

        // Adapter real
        val adapter = AcervoAdapter(livros) { livro ->
                val fragment = LivroFragment.newInstance(livro)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()

        }

        pastaRv.adapter = adapter
        pastaRv.layoutManager = LinearLayoutManager(requireContext())

        pastaFab.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, AcervoFragment())
                .addToBackStack(null)
                .commit()
        }

        pastaEditBtn.setOnClickListener {
            DialogEditarPasta { novoNome ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        editNome(novoNome)
                        pastaTitulo.text = novoNome
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Não foi possivel mudar o nome da pasta.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }.show(parentFragmentManager, "EditarPasta")
        }


        pastaMembrosBtn.setOnClickListener {
            NovoMembroPastaDialog().show(parentFragmentManager, "Membros")
        }

        pastaDeletarBtn.setOnClickListener {
            val dialog = DialogConfirmarDeletarPasta.newInstance(
                nome = pasta.nome ?: "pasta"
            ) {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        FolderManager.deleteFolder(pasta)

                        Toast.makeText(
                            requireContext(),
                            "Pasta deletada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()

                        requireActivity().onBackPressedDispatcher.onBackPressed()

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Não foi possível deletar a pasta.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            dialog.show(parentFragmentManager, "ConfirmarDelete")
        }


        pastaVoltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("pasta")
    }

    companion object {
        private const val ARG_PASTA = "pasta"

        fun newInstance(pasta: Folder): PastaFragment {
            val fragment = PastaFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_PASTA, pasta)
            fragment.arguments = bundle
            return fragment
        }
    }

    suspend fun editNome(novo: String){
        FolderManager.changeFolderName(pasta, novo)
    }

    suspend fun deletePasta(novo: String){
        FolderManager.deleteFolder(pasta)
    }
}
