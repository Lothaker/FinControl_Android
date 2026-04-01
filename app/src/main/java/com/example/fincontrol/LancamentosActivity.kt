package com.example.fincontrol

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fincontrol.data.CategoryRepository
import com.example.fincontrol.data.SessionManager
import com.example.fincontrol.data.TransactionRepository
import com.example.fincontrol.model.Category
import com.example.fincontrol.model.TransactionType
import com.example.fincontrol.util.CurrencyUtils
import com.example.fincontrol.util.DateUtils
import java.time.LocalDate

class LancamentosActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var transactionRepository: TransactionRepository

    private var selectedDate: LocalDate = LocalDate.now()
    private var tipoLancamento: TransactionType = TransactionType.EXPENSE
    private var expenseCategories: List<Category> = emptyList()
    private var incomeCategories: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        sessionManager = SessionManager(this)
        categoryRepository = CategoryRepository(this)
        transactionRepository = TransactionRepository(this)
        val userId = sessionManager.getLoggedUserId() ?: run {
            goToLogin()
            return
        }

        expenseCategories = categoryRepository.getByType(TransactionType.EXPENSE)
        incomeCategories = categoryRepository.getByType(TransactionType.INCOME)

        val tvValor = findViewById<TextView>(R.id.tvValor)
        val etValorManual = findViewById<EditText>(R.id.etValorManual)
        val tvDataSelecionada = findViewById<TextView>(R.id.tvDataSelecionada)
        val btnMais1 = findViewById<Button>(R.id.btnMais1)
        val btnMais10 = findViewById<Button>(R.id.btnMais10)
        val btnMais50 = findViewById<Button>(R.id.btnMais50)
        val btnMais100 = findViewById<Button>(R.id.btnMais100)
        val btnHoje = findViewById<Button>(R.id.btnHoje)
        val btnOntem = findViewById<Button>(R.id.btnOntem)
        val btnPeriodo = findViewById<Button>(R.id.btnPeriodo)
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerCategoria)
        val checkRecorrente = findViewById<CheckBox>(R.id.checkRecorrente)
        val btnDespesa = findViewById<Button>(R.id.btnDespesa)
        val btnReceita = findViewById<Button>(R.id.btnReceita)
        val btnCadastrar = findViewById<Button>(R.id.btnAdicionar)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)

        fun currentCategories(): List<Category> = if (tipoLancamento == TransactionType.EXPENSE) expenseCategories else incomeCategories
        fun bindCategories() {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currentCategories().map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategoria.adapter = adapter
        }
        fun refreshValuePreview() {
            val cents = CurrencyUtils.parseToCents(etValorManual.text.toString()) ?: 0L
            tvValor.text = CurrencyUtils.formatFromCents(cents)
        }
        fun setDate(date: LocalDate) {
            selectedDate = date
            tvDataSelecionada.text = "Data selecionada: ${DateUtils.format(date)}"
        }
        fun configureTypeButtons() {
            val despesaAtiva = tipoLancamento == TransactionType.EXPENSE
            btnDespesa.alpha = if (despesaAtiva) 1f else 0.6f
            btnReceita.alpha = if (despesaAtiva) 0.6f else 1f
        }
        fun addAmount(value: Long) {
            val atual = CurrencyUtils.parseToCents(etValorManual.text.toString()) ?: 0L
            etValorManual.setText(((atual + value) / 100.0).toString().replace('.', ','))
            refreshValuePreview()
        }

        setDate(LocalDate.now())
        bindCategories()
        configureTypeButtons()
        refreshValuePreview()

        btnMais1.setOnClickListener { addAmount(100) }
        btnMais10.setOnClickListener { addAmount(1000) }
        btnMais50.setOnClickListener { addAmount(5000) }
        btnMais100.setOnClickListener { addAmount(10000) }

        etValorManual.addTextChangedListener(SimpleTextWatcher { refreshValuePreview() })

        btnHoje.setOnClickListener { setDate(LocalDate.now()) }
        btnOntem.setOnClickListener { setDate(LocalDate.now().minusDays(1)) }
        btnPeriodo.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day -> setDate(LocalDate.of(year, month + 1, day)) },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            ).show()
        }

        btnDespesa.setOnClickListener {
            tipoLancamento = TransactionType.EXPENSE
            configureTypeButtons()
            bindCategories()
        }
        btnReceita.setOnClickListener {
            tipoLancamento = TransactionType.INCOME
            configureTypeButtons()
            bindCategories()
        }

        btnCadastrar.setOnClickListener {
            val cents = CurrencyUtils.parseToCents(etValorManual.text.toString())
            if (cents == null || cents <= 0) {
                toast("Informe um valor válido.")
                return@setOnClickListener
            }
            val categories = currentCategories()
            if (categories.isEmpty()) {
                toast("Nenhuma categoria disponível para esse tipo.")
                return@setOnClickListener
            }
            val category = categories[spinnerCategoria.selectedItemPosition]
            val result = transactionRepository.saveTransaction(
                TransactionRepository.SaveTransactionRequest(
                    userId = userId,
                    categoryId = category.id,
                    amountCents = cents,
                    type = tipoLancamento,
                    date = selectedDate,
                    description = etDescricao.text.toString().trim().ifBlank { null },
                    recurring = checkRecorrente.isChecked
                )
            )
            result.onSuccess {
                toast(if (checkRecorrente.isChecked) "Lançamento recorrente salvo." else "Lançamento salvo.")
                startActivity(Intent(this, ExtratoActivity::class.java))
                finish()
            }.onFailure {
                toast(it.message ?: "Erro ao salvar lançamento.")
            }
        }

        findViewById<ImageView>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.btnLogout).setOnClickListener {
            sessionManager.clear()
            goToLogin()
        }
        findViewById<ImageView>(R.id.btnCarteira).setOnClickListener { startActivity(Intent(this, CarteiraActivity::class.java)); finish() }
        findViewById<ImageView>(R.id.btnGrafico).setOnClickListener { }
        findViewById<ImageView>(R.id.btnExtrato).setOnClickListener { startActivity(Intent(this, ExtratoActivity::class.java)); finish() }
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun goToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
