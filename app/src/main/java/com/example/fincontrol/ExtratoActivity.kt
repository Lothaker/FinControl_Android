package com.example.fincontrol

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ExtratoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        // VOLTAR → voltar para Carteira
        findViewById<ImageView>(R.id.btnVoltar).setOnClickListener {
            startActivity(Intent(this, CarteiraActivity::class.java))
            finish()
        }

        // LOGOUT → voltar para login
        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // RecyclerView com dados de exemplo
        val rv = findViewById<RecyclerView>(R.id.rvExtrato)
        rv.layoutManager = LinearLayoutManager(this)
        val itens = listOf(
            Pair("↓", "R$ 35,00   Farmácia"),
            Pair("↓", "R$ 120,00  Supermercado"),
            Pair("↑", "R$ 1.500,00 Salário"),
            Pair("↓", "R$ 80,00   Conta de luz")
        )

        rv.adapter = object : RecyclerView.Adapter<ExtratoVH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                ExtratoVH(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_extrato, parent, false)
                )

            override fun getItemCount() = itens.size

            override fun onBindViewHolder(holder: ExtratoVH, position: Int) {
                val (seta, desc) = itens[position]
                holder.tvSeta.text = seta
                holder.tvSeta.setTextColor(
                    if (seta == "↓") 0xFFEA4335.toInt() else 0xFF34A853.toInt()
                )
                holder.tvDescricao.text = desc
            }
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
            // já está no Extrato
        }
    }
}

class ExtratoVH(view: View) : RecyclerView.ViewHolder(view) {
    val tvSeta: TextView = view.findViewById(R.id.tvSeta)
    val tvDescricao: TextView = view.findViewById(R.id.tvDescricao)
}