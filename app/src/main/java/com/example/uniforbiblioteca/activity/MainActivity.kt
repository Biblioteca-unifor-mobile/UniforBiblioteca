package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.uniforbiblioteca.fragment.MenuFragment
import com.example.uniforbiblioteca.fragment.PastasFragment
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.fragment.HistoricoFragment
import com.example.uniforbiblioteca.fragment.HomeFragment
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    lateinit var homeBtn: MaterialButton
    lateinit var historicoBtn: MaterialButton
    lateinit var pastasBtn: MaterialButton
    lateinit var menuBtn: MaterialButton
    lateinit var fm: FragmentManager

    lateinit var bottomMenu: LinearLayout

    var state = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeBtn = findViewById(R.id.homeButton)
        historicoBtn = findViewById(R.id.historicoBtn)
        pastasBtn = findViewById(R.id.pastasBtn)
        menuBtn = findViewById(R.id.menuBtn)
        fm = supportFragmentManager
        bottomMenu = findViewById(R.id.bottomMenu)
    }

    override fun onStart() {
        super.onStart()

        homeBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.mainFragmentContainer, HomeFragment::class.java, null)
                .commit()
        }


        historicoBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.mainFragmentContainer, HistoricoFragment::class.java, null)
                .commit()
        }
        pastasBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.mainFragmentContainer, PastasFragment::class.java, null)
                .commit()
        }
        menuBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.mainFragmentContainer, MenuFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }
    }

    fun changeState(next: String){
        state = next

        if (state == "home"){
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            historicoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            pastasBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            showBottomNav()
        } else if (state == "historico"){
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            historicoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            pastasBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            showBottomNav()
        } else if (state == "pastas" || state == "pasta" ) {
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            historicoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            pastasBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            showBottomNav()
        } else if (state == "acervo") {
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            historicoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            pastasBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            showBottomNav()
        } else {
            hideBottomNav()
        }
    }

    private fun showBottomNav() {
        bottomMenu.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomMenu.visibility = View.GONE
    }


    fun sair(){
        val intencao = Intent(this, LoginActivity::class.java)
        startActivity(intencao)
    }
}