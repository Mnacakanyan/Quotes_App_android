package com.quotes.quotesapp.data.repository

import com.quotes.quotesapp.data.db.QuoteDao
import com.quotes.quotesapp.data.db.QuoteEntity
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val quoteDao: QuoteDao
): FavoritesRepository {

    private fun QuoteEntity.toPresentationModel(): QuoteUiModel {
        return QuoteUiModel(

            quote = this.quote,
            author = this.author
        )
    }


    private fun QuoteUiModel.toEntity(): QuoteEntity {
        return QuoteEntity(

            quote = this.quote,
            author = this.author,

        )
    }

    override fun getFavoriteQuotes(): Flow<List<QuoteUiModel>> {
        return quoteDao.getAllQuotes().map { entities ->
            entities.map { it.toPresentationModel() }
        }
    }

    override suspend fun addFavorite(quote: QuoteUiModel) {

        quoteDao.insertQuote(quote.toEntity())
    }

    override suspend fun removeFavorite(quote: QuoteUiModel) {


    }

    suspend fun isFavorite(quote: QuoteUiModel): Boolean {
        return false
    }
}
