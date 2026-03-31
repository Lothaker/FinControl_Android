package com.example.fincontrol

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LancamentosActivity : AppCompatActivity() {

    private var valorCentavos: Long = 0L
    private var tipoLancamento: String = "receita"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        val tvValor          = findViewById<TextView>(R.id.tvValor)
        val btnMais1         = findViewById<Button>(R.id.btnMais1)
        val btnMais10        = findViewById<Button>(R.id.btnMais10)
        val btnMais50        = findViewById<Button>(R.id.btnMais50)
        val btnMais100       = findViewById<Button>(R.id.btnMais100)
        val btnHoje          = findViewById<Button>(R.id.btnHoje)
        val btnOntem         = findViewById<Button>(R.id.btnOntem)
        val btnPeriodo       = findViewById<Button>(R.id.btnPeriodo)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val checkRecorrente  = findViewById<CheckBox>(R.id.checkRecorrente)
        val btnDespesa       = findViewById<Button>(R.id.btnDespesa)
        val btnReceita       = findViewById<Button>(R.id.btnReceita)
        val btnCadastrar     = findViewById<Button>(R.id.btnAdicionar)

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

        // Spinner
        val categorias = listOf(
            "Categorias:",
            "Alimentação",
            "Saúde",
            "Transporte",
            "Lazer",
            "Educação",
            "Moradia",
            "Outros"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        // Valor
        fun atualizar() {
            tvValor.text = "R$: %d,%02d".format(valorCentavos / 100, valorCentavos % 100)
        }

        btnMais1.setOnClickListener   { valorCentavos += 100; atualizar() }
        btnMais10.setOnClickListener  { valorCentavos += 1000; atualizar() }
        btnMais50.setOnClickListener  { valorCentavos += 5000; atualizar() }
        btnMais100.setOnClickListener { valorCentavos += 10000; atualizar() }

        // Data
        btnHoje.setOnClickListener { Toast.makeText(this, "Data: Hoje", Toast.LENGTH_SHORT).show() }
        btnOntem.setOnClickListener { Toast.makeText(this, "Data: Ontem", Toast.LENGTH_SHORT).show() }
        btnPeriodo.setOnClickListener { Toast.makeText(this, "Selecionar período", Toast.LENGTH_SHORT).show() }

        // Tipo
        btnDespesa.setOnClickListener { tipoLancamento = "despesa" }
        btnReceita.setOnClickListener { tipoLancamento = "receita" }

        // Cadastrar
        btnCadastrar.setOnClickListener {
            if (valorCentavos == 0L) {
                Toast.makeText(this, "Informe um valor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (spinnerCategoria.selectedItem.toString() == "Categorias:") {
                Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Lançamento cadastrado!", Toast.LENGTH_SHORT).show()
            valorCentavos = 0L
            atualizar()
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