package com.example.uniforbiblioteca.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.uniforbiblioteca.fragment.AdminHomeFragment
import com.example.uniforbiblioteca.fragment.AdminUsersFragment
import com.example.uniforbiblioteca.R
import com.example.uniforbiblioteca.fragment.AdminAcervo
import com.example.uniforbiblioteca.viewmodel.FolderManager
import com.google.android.material.button.MaterialButton

class AdminActivity : AppCompatActivity() {

    lateinit var voltar: Button
    lateinit var homeBtn: MaterialButton
    lateinit var usersBtn: MaterialButton
    lateinit var acervoBtn: MaterialButton
    lateinit var fm: FragmentManager

    lateinit var bottomMenu: LinearLayout

    var state = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminHomeFragment())
                .commit()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeBtn = findViewById(R.id.adminHomeButton)
        acervoBtn = findViewById(R.id.adminAcervoButton)
        usersBtn = findViewById(R.id.adminUsersButton)
        voltar = findViewById(R.id.admin_voltar)
        fm = supportFragmentManager
        bottomMenu = findViewById(R.id.bottomMenu)
    }

    override fun onStart() {
        super.onStart()

        homeBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminHomeFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        acervoBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminAcervo::class.java, null)
                .addToBackStack(null)
                .commit()
        }


        usersBtn.setOnClickListener {
            fm.beginTransaction()
                .replace(R.id.adminFragmentContainer, AdminUsersFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }


        voltar.setOnClickListener {
            onBackPressed()
        }

    }

    fun changeState(next: String){
        state = next

        if (state == "home"){
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            usersBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            acervoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            voltar.visibility = View.GONE
            showBottomNav()
        } else if (state == "users"){
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            usersBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            acervoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            voltar.visibility = View.VISIBLE
            showBottomNav()
        } else if (state == "acervo") {
            homeBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            usersBtn.iconTint = ContextCompat.getColorStateList(this, R.color.black)
            acervoBtn.iconTint = ContextCompat.getColorStateList(this, R.color.grey)
            voltar.visibility = View.VISIBLE

            showBottomNav()
        } else {
            voltar.visibility = View.VISIBLE
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