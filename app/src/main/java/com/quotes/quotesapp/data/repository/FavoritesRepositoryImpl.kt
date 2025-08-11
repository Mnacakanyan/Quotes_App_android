package com.quotes.quotesapp.data.repository

import com.quotes.quotesapp.data.db.QuoteDao
import com.quotes.quotesapp.data.mapper.toDomainQuote
import com.quotes.quotesapp.data.mapper.toEntity
import com.quotes.quotesapp.domain.model.Quote
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val quoteDao: QuoteDao
) : FavoritesRepository {

    override fun getFavoriteQuotes(): Flow<List<Quote>> {
        return quoteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomainQuote() }
        }
    }

    override suspend fun addFavorite(quote: Quote) {
        quoteDao.insertQuote(quote.toEntity())
    }

    override suspend fun removeFavorite(quote: Quote) {


    }

    override suspend fun deleteQuote(id: Int) {
        quoteDao.deleteQuote(id)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return quoteDao.isFavorite(id)
    }


}
