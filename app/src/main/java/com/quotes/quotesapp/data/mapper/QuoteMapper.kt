package com.quotes.quotesapp.data.mapper

import com.quotes.quotesapp.data.model.QuoteResponse
import com.quotes.quotesapp.domain.model.Quote

fun QuoteResponse.toQuote(): Quote {
    return Quote(
        author = this.author,
        quote = this.quote
    )
}