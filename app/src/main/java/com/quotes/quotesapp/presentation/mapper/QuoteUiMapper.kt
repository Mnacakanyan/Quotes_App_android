package com.quotes.quotesapp.presentation.mapper

import com.quotes.quotesapp.domain.model.Quote
import com.quotes.quotesapp.presentation.model.QuoteUiModel

fun Quote.toQuoteUiModel(): QuoteUiModel {
    return QuoteUiModel(
        id = id,
        author = author,
        quote = quote
    )
}

fun QuoteUiModel.toQuote(): Quote {
    return Quote(
        id = id,
        author = author,
        quote = quote
    )
}
