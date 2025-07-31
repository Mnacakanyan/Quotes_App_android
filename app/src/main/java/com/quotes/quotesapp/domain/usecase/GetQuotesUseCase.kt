package com.quotes.quotesapp.domain.usecase

import com.quotes.quotesapp.domain.model.Quote
import com.quotes.quotesapp.domain.repository.QuoteRepository

class GetQuotesUseCase(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(): List<Quote> {
        return quoteRepository.getQuotes()
    }
}