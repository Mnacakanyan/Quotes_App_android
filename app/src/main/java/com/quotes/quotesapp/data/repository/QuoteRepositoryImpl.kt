package com.quotes.quotesapp.data.repository

import com.quotes.quotesapp.data.mapper.toQuote
import com.quotes.quotesapp.data.model.QuoteResponse
import com.quotes.quotesapp.domain.model.Quote
import com.quotes.quotesapp.domain.repository.QuoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class QuoteRepositoryImpl(
    private val httpClient: HttpClient
): QuoteRepository {
    override suspend fun getDailyQuote(): Quote {
        return httpClient.get(
            urlString = "https://zenquotes.io/api/random"
        ).body<List<QuoteResponse>>().first().toQuote()
    }
}