package com.example.uniforbiblioteca.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.dataclass.Folder
import com.example.uniforbiblioteca.dialog.DialogCriarPasta
import com.example.uniforbiblioteca.rvadapter.PastaAdapter
import com.example.uniforbiblioteca.viewmodel.FolderManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PastasFragment : androidx.fragment.app.Fragment() {


    lateinit var addFAB: FloatingActionButton



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pastas, container, false)

        //
        val recyclerView: RecyclerView = view.findViewById(R.id.pastasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Adapter
        val adapter = PastaAdapter(FolderManager.folderList) { pasta ->
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.mainFragmentContainer,
                    PastaFragment.newInstance(pasta)
                )
                .addToBackStack(null)
                .commit()
        }


        recyclerView.adapter = adapter

        addFAB = view.findViewById(R.id.novaPasta)

        addFAB.setOnClickListener {
            val dialog = DialogCriarPasta { novoNome ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        createFolder(novoNome)
                        // Atualiza lista
                        adapter.updateItems(FolderManager.folderList)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(),
                            "Não foi possível criar a pasta.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            dialog.show(parentFragmentManager, "CriarPasta")
        }


        lifecycleScope.launch {
            try {
                FolderManager.updateFolderList()
            } catch (e: Exception){
                Toast.makeText(requireContext(), "Ocorreu um erro ao buscar as pastas.", Toast.LENGTH_SHORT).show()
            }
            adapter.updateItems(FolderManager.folderList)
        }

        return view
    }


    suspend fun createFolder(novo: String) {
        val folder = Folder(nome = novo)
        FolderManager.createFolder(folder)
    }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("pastas")
    }
}