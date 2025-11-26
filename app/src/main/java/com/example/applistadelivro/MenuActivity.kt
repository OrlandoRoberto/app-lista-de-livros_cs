package com.example.applistadelivro

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// ⭐ CLASSE para representar cada livro (FORA da MenuActivity)
data class Livro(
    val titulo: String,
    val autor: String,
    val categoria: String,
    val preco: Double
)

class MenuActivity : AppCompatActivity() {

    // ⭐ LISTA COMPLETA E ATUALIZADA DE TODOS OS LIVROS
    private val todosLivros = listOf(
        // MENTALIDADE (8 livros)
        Livro("Quem Pensa Enriquece", "Napoleon Hill", "Mentalidade", 49.90),
        Livro("O Homem mais Rico da Babilônia", "George Samuel Clason", "Mentalidade", 39.99),
        Livro("Mindset", "Carol Dweck", "Mentalidade", 52.67),
        Livro("Mais Esperto que o Diabo", "Napoleon Hill", "Mentalidade", 25.00),
        Livro("A Sutil Arte de Ligar o Foda-se", "Mark Manson", "Mentalidade", 15.00),
        Livro("A Coragem de Ser Imperfeito", "Brené Brown", "Mentalidade", 46.41),
        Livro("Comece Pelo Porquê", "Simon Sinek", "Mentalidade", 30.00),
        Livro("Essencialismo", "Greg Mckeown", "Mentalidade", 35.80),

        // HÁBITOS (10 livros)
        Livro("O Poder do Hábito", "Charles Duhigg", "Hábitos", 44.99),
        Livro("Hábitos Atómicos", "James Clear", "Hábitos", 39.99),
        Livro("Não é você são seus Hábitos", "Luciano do Carmo Gomes", "Hábitos", 48.59),
        Livro("Mudança de Hábitos", "Igor lins Lemos", "Hábitos", 42.20),
        Livro("O Milagre do Amanhã", "Hal Elrod", "Hábitos", 11.96),
        Livro("Os 7 Hábitos das Pessoas Altamente Eficazes", "Stephen R. Covey", "Hábitos", 55.92),
        Livro("Os Hábitos Secretos dos Gênios", "Craig M. Wright", "Hábitos", 53.31),
        Livro("Mini-Hábitos", "Stephen Guise", "Hábitos", 34.90),
        Livro("365 Hábitos Poderosos", "Paulo Houch", "Hábitos", 8.24),
        Livro("Bons Hábitos Maus Hábitos", "Wendy Wood", "Hábitos", 44.90),

        // FINANÇAS (10 livros)
        Livro("A Psicologia Financeira", "Morgan Housel", "Finanças", 49.99),
        Livro("Os Segredos da Mente Milionária", "T. Harv Eker", "Finanças", 38.13),
        Livro("Pai Rico Pai Pobre", "Robert Kiosaki e Sharon L.Lechter", "Finanças", 38.40),
        Livro("O poder da Ação", "Paulo Vieira", "Finanças", 25.00),
        Livro("Me Poupe", "Nathalia Arcuri", "Finanças", 20.00),
        Livro("Finanças pessoais", "Edimilson Santos Assunção", "Finanças", 44.20),
        Livro("Do mil ao milhão", "Thiago Nigro", "Finanças", 32.52),
        Livro("Criação de Riqueza", "Paulo Vieira", "Finanças", 32.52),
        Livro("O Investidor Inteligente", "Benjamin Graham", "Finanças", 58.89),
        Livro("Dinheiro, Dívida e Finanças", "Jim Newheiser", "Finanças", 40.00)
    )

    private var categoriaSelecionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarSpinner()
        configurarSearchView()
    }

    private fun configurarSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerCategorias)
        val categorias = arrayOf(
            "Selecione uma categoria",
            "Mentalidade",
            "Hábitos",
            "Finanças"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    categoriaSelecionada = categorias[position]
                    abrirTelaDaCategoria(categoriaSelecionada, "")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun configurarSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                buscarLivroEmTodasCategorias(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    // ⭐ FUNÇÃO DE BUSCA MELHORADA (ignora acentos)
    private fun buscarLivroEmTodasCategorias(termoPesquisa: String) {
        val termo = removerAcentos(termoPesquisa.lowercase())

        // Procura o livro em TODA a lista (ignorando acentos)
        val livroEncontrado = todosLivros.find { livro ->
            removerAcentos(livro.titulo.lowercase()).contains(termo) ||
                    removerAcentos(livro.autor.lowercase()).contains(termo)
        }

        if (livroEncontrado != null) {
            Toast.makeText(this, "Livro encontrado em ${livroEncontrado.categoria}!", Toast.LENGTH_SHORT).show()
            abrirTelaDaCategoria(livroEncontrado.categoria, termoPesquisa)
        } else {
            Toast.makeText(this, "Livro não encontrado. Tente outro termo.", Toast.LENGTH_SHORT).show()
        }
    }

    // ⭐ FUNÇÃO NOVA: Remove acentos e caracteres especiais
    private fun removerAcentos(texto: String): String {
        return texto
            .replace("á", "a").replace("à", "a").replace("â", "a").replace("ã", "a")
            .replace("é", "e").replace("è", "e").replace("ê", "e")
            .replace("í", "i").replace("ì", "i").replace("î", "i")
            .replace("ó", "o").replace("ò", "o").replace("ô", "o").replace("õ", "o")
            .replace("ú", "u").replace("ù", "u").replace("û", "u")
            .replace("ç", "c")
            .replace("Á", "A").replace("À", "A").replace("Â", "A").replace("Ã", "A")
            .replace("É", "E").replace("È", "E").replace("Ê", "E")
            .replace("Í", "I").replace("Ì", "I").replace("Î", "I")
            .replace("Ó", "O").replace("Ò", "O").replace("Ô", "O").replace("Õ", "O")
            .replace("Ú", "U").replace("Ù", "U").replace("Û", "U")
            .replace("Ç", "C")
    }

    private fun abrirTelaDaCategoria(categoria: String, termoPesquisa: String) {
        val intent = when (categoria) {
            "Mentalidade" -> Intent(this, MainActivity::class.java)
            "Hábitos" -> Intent(this, habitosActivity::class.java)
            "Finanças" -> Intent(this, FinancasActivity::class.java)
            else -> null
        }

        if (intent != null) {
            intent.putExtra("TERMO_PESQUISA", termoPesquisa)
            intent.putExtra("CATEGORIA", categoria)
            startActivity(intent)
        }
    }
}