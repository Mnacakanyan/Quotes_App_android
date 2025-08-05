package com.quotes.quotesapp.data.repository

import com.quotes.quotesapp.data.db.QuoteDao
import com.quotes.quotesapp.data.db.QuoteEntity // Assuming QuoteEntity is in this package
import com.quotes.quotesapp.presentation.model.QuoteUiModel // Assuming this is the correct QuoteUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val quoteDao: QuoteDao
) {

    // Private mapping function from QuoteEntity to QuoteUiModel
    private fun QuoteEntity.toPresentationModel(): QuoteUiModel {
        return QuoteUiModel(

            quote = this.quote,
            author = this.author
        )
    }

    // Private mapping function from QuoteUiModel to QuoteEntity
    private fun QuoteUiModel.toEntity(): QuoteEntity {
        return QuoteEntity(

            quote = this.quote,
            author = this.author,

        )
    }

    fun getFavoriteQuotes(): Flow<List<QuoteUiModel>> {
        return quoteDao.getAllQuotes().map { entities ->
            entities.map { it.toPresentationModel() } // Use internal mapper
        }
    }

    suspend fun addFavorite(quote: QuoteUiModel) {

        quoteDao.insertQuote(quote.toEntity()) // Use internal mapper
    }

    suspend fun removeFavorite(quote: QuoteUiModel) {


    }

    suspend fun isFavorite(quote: QuoteUiModel): Boolean {
        return false
    }
}
