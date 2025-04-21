package com.quotes.quotesapp.presentation.viewmodel

import com.quotes.quotesapp.presentation.model.QuoteUiModel

data class QuoteState(
    val quote: QuoteUiModel = QuoteUiModel(),
    val isLoading: Boolean = false,
    val error: String? = null
)