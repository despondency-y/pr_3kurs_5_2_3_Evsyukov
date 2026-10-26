package com.example.pr_3kurs_5_2_3_evsyukov

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Button

class LibraryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        db = AppDatabase.getDatabase(this)

        val rvBooks = findViewById<RecyclerView>(R.id.rvBooks)
        rvBooks.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter()
        rvBooks.adapter = adapter

        loadBooks()
    }

    private fun loadBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val books = db.bookDao().getAllBooks()
            runOnUiThread {
                adapter.setBooks(books)
            }
        }
    }

    inner class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
        private var books: List<Book> = emptyList()

        fun setBooks(newBooks: List<Book>) {
            books = newBooks
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return BookViewHolder(view)
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            val book = books[position]
            holder.text1.text = book.title
            holder.text2.text = "Автор: ${book.author}, ISBN: ${book.isbn}"

            // Удаление по долгому нажатию
            holder.itemView.setOnLongClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    db.bookDao().deleteBook(book)
                    loadBooks()
                }
                true
            }
        }

        override fun getItemCount() = books.size

        inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val text1: TextView = view.findViewById(android.R.id.text1)
            val text2: TextView = view.findViewById(android.R.id.text2)
        }
    }
}