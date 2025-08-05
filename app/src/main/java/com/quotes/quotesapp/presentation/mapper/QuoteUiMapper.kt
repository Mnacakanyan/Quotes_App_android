package com.quotes.quotesapp.presentation.mapper

import com.quotes.quotesapp.domain.model.Quote
import com.quotes.quotesapp.presentation.model.QuoteUiModel

fun Quote.toQuoteUiModel(): QuoteUiModel {
    return QuoteUiModel(
        author = author,
        quote = quote
    )
}

