package com.example.pr_3kurs_5_2_3_evsyukov

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etIsbn: EditText
    private lateinit var tvBookInfo: TextView
    private lateinit var btnSaveBook: Button
    private var currentBook: Book? = null
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)

        etIsbn = findViewById(R.id.etIsbn)
        tvBookInfo = findViewById(R.id.tvBookInfo)
        btnSaveBook = findViewById(R.id.btnSaveBook)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnGoToLibrary = findViewById<Button>(R.id.btnGoToLibrary)

        btnSearch.setOnClickListener {
            val isbn = etIsbn.text.toString().trim()
            if (isbn.isNotEmpty()) {
                searchBook(isbn)
            } else {
                Toast.makeText(this, "Введите ISBN", Toast.LENGTH_SHORT).show()
            }
        }

        btnSaveBook.setOnClickListener {
            currentBook?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    db.bookDao().insertBook(it)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Книга сохранена!", Toast.LENGTH_SHORT).show()
                        btnSaveBook.visibility = android.view.View.GONE
                    }
                }
            }
        }

        btnGoToLibrary.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
    }

    private fun searchBook(isbn: String) {
        // URL для OpenLibrary API
        val url = "https://openlibrary.org/api/books?bibkeys=ISBN:$isbn&format=json&jscmd=data"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val bookKey = "ISBN:$isbn"

                    if (jsonObject.has(bookKey)) {
                        val bookData = jsonObject.getJSONObject(bookKey)

                        val title = bookData.optString("title", "Неизвестно")
                        val publishDate = bookData.optString("publish_date", "Неизвестно")

                        var author = "Неизвестен"
                        if (bookData.has("authors")) {
                            val authorsArray = bookData.getJSONArray("authors")
                            if (authorsArray.length() > 0) {
                                author = authorsArray.getJSONObject(0).optString("name", "Неизвестен")
                            }
                        }

                        currentBook = Book(isbn = isbn, title = title, author = author, publishDate = publishDate)

                        tvBookInfo.text = """
                            Название: $title
                            Автор: $author
                            Дата издания: $publishDate
                        """.trimIndent()

                        btnSaveBook.visibility = android.view.View.VISIBLE
                    } else {
                        tvBookInfo.text = "Книга с таким ISBN не найдена."
                        btnSaveBook.visibility = android.view.View.GONE
                    }
                } catch (e: Exception) {
                    Log.e("MyLog", "Ошибка парсинга: ${e.message}")
                    tvBookInfo.text = "Ошибка обработки данных."
                }
            },
            { error ->
                Log.e("MyLog", "Volley error: $error")
                tvBookInfo.text = "Ошибка сети. Проверьте подключение."
            }
        )
        queue.add(stringRequest)
    }
}