package com.quotes.quotesapp.domain.repository

import com.quotes.quotesapp.domain.model.Quote

interface QuoteRepository {
    suspend fun getDailyQuote(): Quote
}