package com.quotes.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotes.quotesapp.data.db.QuoteDao
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import com.quotes.quotesapp.domain.usecase.GetQuoteUseCase
import com.quotes.quotesapp.domain.usecase.GetQuotesUseCase
import com.quotes.quotesapp.presentation.mapper.toQuote
import com.quotes.quotesapp.presentation.mapper.toQuoteUiModel
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getQuotesUseCase: GetQuotesUseCase,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuoteState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<QuoteEvent?>()
    val events = _events.asSharedFlow()

    fun fetchQuotes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { oldState -> oldState.copy(isLoading = true) }
            val dailyQuoteDeferred = async { getQuoteUseCase() }
            val quotesDeferred = async { getQuotesUseCase() }

            val dailyQuote = dailyQuoteDeferred.await()
            val quotes = quotesDeferred.await()

            setState { oldState ->
                oldState.copy(
                    quote = dailyQuote.toQuoteUiModel(),
                    quotes = quotes.map { it.toQuoteUiModel() },
                    isLoading = false
                )
            }

        }
    }

    fun onAction(action: QuoteAction) {
        when (action) {
            is QuoteAction.SwipeLeft -> {
                addToFavorites(action.model)
            }
            is QuoteAction.SwipeRight -> {
                removeFromList(action.model)
            }

            is QuoteAction.Like -> {
                addToFavorites(action.model)
            }
            is QuoteAction.Share -> {
                viewModelScope.launch {
                    _events.emit(QuoteEvent.Share(action.model.quote, action.model.author))
                }
            }
            is QuoteAction.Delete -> {
                deleteQuote(action.id)
            }
        }
    }

    fun onEventHandled() {
        viewModelScope.launch {
            _events.emit(null)
        }
    }

    private fun removeFromList(model: QuoteUiModel) {
        setState { oldState ->
            oldState.copy(
                quotes = oldState.quotes.filter { it != model }
            )
        }
    }

    private fun addToFavorites(model: QuoteUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.addFavorite(quote = model.toQuote())
            
        }

    }

    private fun deleteQuote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesRepository.deleteQuote(id = id)
        }
    }



    private fun setState(block: (QuoteState) -> QuoteState) {
        _state.value = block(_state.value)
    }
}