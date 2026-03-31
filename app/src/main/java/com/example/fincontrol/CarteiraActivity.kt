package com.example.fincontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CarteiraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carteira)

        // Botão ADICIONAR → LançamentosActivity
        findViewById<Button>(R.id.btnAdicionar).setOnClickListener {
            startActivity(Intent(this, LancamentosActivity::class.java))
        }

        // LOGOUT → voltar para login
        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        // Navbar
        findViewById<ImageView>(R.id.btnCarteira).setOnClickListener {
            startActivity(Intent(this, CarteiraActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btnGrafico).setOnClickListener {
            startActivity(Intent(this, LancamentosActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.btnExtrato).setOnClickListener {
            startActivity(Intent(this, ExtratoActivity::class.java))
            finish()
        }
    }
}