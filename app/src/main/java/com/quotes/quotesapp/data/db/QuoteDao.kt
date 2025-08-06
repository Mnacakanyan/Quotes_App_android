package com.quotes.quotesapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("DELETE FROM quote_database WHERE id = :id")
    suspend fun deleteQuote(id: Int)

    @Query("SELECT * FROM quote_database")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quote_database WHERE favorite = 1")
    fun getAllFavorites(): Flow<List<QuoteEntity>>

    @Delete
    suspend fun deleteQuote(quote: QuoteEntity)


}