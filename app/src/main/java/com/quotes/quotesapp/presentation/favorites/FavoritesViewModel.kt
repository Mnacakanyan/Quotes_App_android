package com.quotes.quotesapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import com.quotes.quotesapp.presentation.mapper.toQuoteUiModel
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val favoriteQuotes: StateFlow<List<QuoteUiModel>> =
        favoritesRepository.getFavoriteQuotes()
            .map { it.map { quote -> quote.toQuoteUiModel() }}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}
