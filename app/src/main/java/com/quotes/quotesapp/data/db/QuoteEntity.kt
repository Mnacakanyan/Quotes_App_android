package com.quotes.quotesapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote_database")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quote: String,
    val author: String,
    val favorite: Boolean = false

)