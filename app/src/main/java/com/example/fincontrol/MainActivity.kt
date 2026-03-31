package com.example.fincontrol

import android.content.Intent
import android.widget.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val nome      = findViewById<EditText>(R.id.nome)
        val senha     = findViewById<EditText>(R.id.senha)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        findViewById<TextView>(R.id.tvEsqueceuSenha).setOnClickListener {
            Toast.makeText(this, "Recuperação de senha em breve", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextView>(R.id.tvCriarConta).setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        btnEntrar.setOnClickListener {
            if (nome.text.isBlank() || senha.text.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, CarteiraActivity::class.java))
            finish()
        }
    }
}