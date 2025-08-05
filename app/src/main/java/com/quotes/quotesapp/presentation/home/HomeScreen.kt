package com.quotes.quotesapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aghajari.compose.lazyswipecards.LazySwipeCards
import com.aghajari.compose.lazyswipecards.SwipeDirection
import com.quotes.quotesapp.presentation.model.QuoteUiModel
import com.quotes.quotesapp.presentation.util.CenteredCircularProgressIndicator
import com.quotes.quotesapp.presentation.viewmodel.MainViewModel
import com.quotes.quotesapp.presentation.viewmodel.QuoteAction
import com.quotes.quotesapp.presentation.viewmodel.QuoteState

@Composable
fun HomeScreen(
    state: QuoteState,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CenteredCircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazySwipeCards(
                cardShape = RoundedCornerShape(16.dp),
                cardColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Adjust weight as needed
            ) {
                onSwiped { item, direction ->
                    when (direction) {
                        SwipeDirection.LEFT -> viewModel.onAction(
                            QuoteAction.SwipeLeft(
                                item as QuoteUiModel
                            )
                        )

                        SwipeDirection.RIGHT -> viewModel.onAction(
                            QuoteAction.SwipeRight(
                                item as QuoteUiModel
                            )
                        )
                    }
                }
                items(
                    state.quotes.toMutableList()
                ) {
                    QuoteCard(
                        quoteString = it.quote,
                        quoteAuthor = it.author,
                        onLike = { viewModel.onAction(QuoteAction.Like(it)) },
                        onShare = { viewModel.onAction(QuoteAction.Share(it)) }
                    )
                }
            }
        }
    }
}