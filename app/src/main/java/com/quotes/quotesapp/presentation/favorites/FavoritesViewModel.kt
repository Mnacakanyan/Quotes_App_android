package com.quotes.quotesapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val favoriteQuotes: StateFlow<List<QuoteUiModel>> =
        favoritesRepository.getFavoriteQuotes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun removeFromFavorites(quoteUiModel: QuoteUiModel) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(quoteUiModel)
        }
    }
}
