package com.example.uniforbiblioteca.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforbiblioteca.activity.MainActivity
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.api.CartAPI
import com.example.uniforbiblioteca.api.RetrofitClient
import com.example.uniforbiblioteca.dataclass.CartCheckoutRequest
import com.example.uniforbiblioteca.dataclass.CartItem
import com.example.uniforbiblioteca.rvadapter.CestaAdapter
import com.example.uniforbiblioteca.dialog.DialogConfirmarPedido
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CestaFragment : Fragment() {
    lateinit var voltar: Button

    lateinit var addFAB: FloatingActionButton

    lateinit var confirmarBtn: Button
    lateinit var txtVazio: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CestaAdapter

    private val cartAPI by lazy {
        RetrofitClient.create(context).create(CartAPI::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cesta, container, false)

        recyclerView = view.findViewById(R.id.cestaRecyclerView)
        txtVazio = view.findViewById(R.id.txt_cesta_vazia)
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CestaAdapter(emptyList()) { item ->
            removerItem(item)
        }
        recyclerView.adapter = adapter

        voltar = view.findViewById(R.id.voltar_cesta)

        voltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        addFAB = view.findViewById(R.id.cesta_add_fab)

        addFAB.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, AcervoFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        confirmarBtn = view.findViewById(R.id.continuar_cesta)

        confirmarBtn.setOnClickListener {
            val dialog = DialogConfirmarPedido.newInstance(false)
            dialog.show(parentFragmentManager, "ConfirmarPedido")
        }

        // Listener para atualizar a cesta quando o checkout for concluído com sucesso no dialog
        parentFragmentManager.setFragmentResultListener("checkout_key", viewLifecycleOwner) { _, bundle ->
            val success = bundle.getBoolean("success")
            if (success) {
                carregarCesta() // Recarrega (deve vir vazio)
                // Opcional: navegar para outra tela ou mostrar feedback
            }
        }

        carregarCesta()

        return view
    }

    private fun carregarCesta() {
        lifecycleScope.launch {
            try {
                // API agora retorna a lista de itens diretamente (List<CartItem>)
                val items = cartAPI.getCart()
                
                if (items.isEmpty()) {
                    txtVazio.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    confirmarBtn.isEnabled = false
                    confirmarBtn.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                } else {
                    txtVazio.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    confirmarBtn.isEnabled = true
                    // Usa uma cor azul padrão já que R.color.blue não existe
                    confirmarBtn.backgroundTintList = ColorStateList.valueOf(Color.BLUE) 
                    adapter.updateData(items)
                }
            } catch (e: Exception) {
                Log.e("CESTA_FRAGMENT", "Erro ao carregar cesta", e)
                Toast.makeText(context, "Erro ao carregar cesta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removerItem(item: CartItem) {
        val bookId = item.bookId ?: return
        lifecycleScope.launch {
            try {
                cartAPI.removeFromCart(bookId)
                Toast.makeText(context, "Item removido", Toast.LENGTH_SHORT).show()
                carregarCesta() // Recarrega a lista
            } catch (e: Exception) {
                Log.e("CESTA_FRAGMENT", "Erro ao remover item", e)
                Toast.makeText(context, "Erro ao remover item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função mantida pública caso o Dialog precise chamá-la no futuro (se implementada interface)
    fun realizarCheckout() {
        // Data limite padrão (ex: 15 dias)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 15)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dataLimite = dateFormat.format(calendar.time)

        val request = CartCheckoutRequest(dataLimite)

        lifecycleScope.launch {
            try {
                val response = cartAPI.checkout(request)
                Toast.makeText(context, response.message ?: "Empréstimo realizado!", Toast.LENGTH_LONG).show()
                
                // Limpa a tela ou navega para home
                adapter.updateData(emptyList())
                parentFragmentManager.popBackStack() // Volta para tela anterior
                
            } catch (e: Exception) {
                Log.e("CESTA_FRAGMENT", "Erro no checkout", e)
                Toast.makeText(context, "Erro ao finalizar empréstimo: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.changeState("cesta")
    }
}
