package com.example.fincontrol

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cadastro)

        val email = findViewById<EditText>(R.id.email)
        val usuario = findViewById<EditText>(R.id.usuario)
        val senha = findViewById<EditText>(R.id.senha)
        val termos = findViewById<CheckBox>(R.id.checkTermos)
        val cadastrar = findViewById<Button>(R.id.btnCadastrar)

        cadastrar.setOnClickListener {

            if(!termos.isChecked){
                Toast.makeText(this,"Aceite os termos",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Conta criada!",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}