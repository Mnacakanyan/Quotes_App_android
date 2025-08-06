package com.quotes.quotesapp.domain.repository

import com.quotes.quotesapp.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavoriteQuotes(): Flow<List<Quote>>

    suspend fun addFavorite(quote: Quote)

    suspend fun removeFavorite(quote: Quote)

    suspend fun deleteQuote(quote: Quote)
}