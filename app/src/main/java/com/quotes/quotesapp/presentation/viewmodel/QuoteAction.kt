package com.quotes.quotesapp.presentation.viewmodel

import com.quotes.quotesapp.presentation.model.QuoteUiModel

sealed interface QuoteAction {
    data class SwipeLeft(val model: QuoteUiModel) : QuoteAction
    data class SwipeRight(val model: QuoteUiModel) : QuoteAction

    data class Like(val model: QuoteUiModel) : QuoteAction
    data class Share(val model: QuoteUiModel) : QuoteAction
    data class Delete(val id: Int) : QuoteAction
}


sealed interface QuoteEvent {
    data class Share(val quoteText: String, val quoteAuthor: String) : QuoteEvent
}