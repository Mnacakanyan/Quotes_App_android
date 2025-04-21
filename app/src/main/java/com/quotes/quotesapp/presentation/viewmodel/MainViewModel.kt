package com.quotes.quotesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotes.quotesapp.domain.usecase.GetQuoteUseCase
import com.quotes.quotesapp.presentation.mapper.toQuoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(QuoteState())
    val state = _state.asStateFlow()

    fun getDailyQuote() {
        io {
            setState { it.copy(isLoading = true) }
            val quote = getQuoteUseCase()
            setState {
                it.copy(
                    quote = quote.toQuoteUiModel(),
                    isLoading = false

                )
            }
        }
    }

    private fun io(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

    private fun setState(block: (QuoteState) -> QuoteState) {
        _state.value = block(_state.value)
    }
}