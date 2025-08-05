package com.quotes.quotesapp.domain.repository

import com.quotes.quotesapp.presentation.model.QuoteUiModel
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavoriteQuotes(): Flow<List<QuoteUiModel>>

    suspend fun addFavorite(quote: QuoteUiModel)

    suspend fun removeFavorite(quote: QuoteUiModel)
}