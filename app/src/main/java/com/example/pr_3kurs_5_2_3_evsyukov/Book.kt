package com.example.pr_3kurs_5_2_3_evsyukov

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val isbn: String,
    val title: String,
    val author: String,
    val publishDate: String
)