package com.quotes.quotesapp.data.mapper

import com.quotes.quotesapp.data.db.QuoteEntity
import com.quotes.quotesapp.data.model.QuoteResponse
import com.quotes.quotesapp.domain.model.Quote

fun QuoteResponse.toDomainQuote(): Quote {
    return Quote(
        author = this.author,
        quote = this.quote
    )
}

fun QuoteEntity.toDomainQuote(): Quote {
    return Quote(
        id = this.id,
        author = this.author,
        quote = this.quote
    )
}

fun Quote.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = this.id,
        author = this.author,
        quote = this.quote
    )
}