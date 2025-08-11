package com.quotes.quotesapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun QuoteCard(
    quoteString: String,
    quoteAuthor: String,
    onDelete: () -> Unit,
    onLike: () -> Unit,
    onShare: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
//                        Color(0xFF849A3B),
//                        Color(0xFFB06436)

                        Color(0xFF081256),
                        Color(0xFF8F7BD9),
                        Color(0xFFFFFFFF),
                    ),
                    tileMode = TileMode.Mirror
                )
            )
    ) {
        val (text, author, heartButton, shareButton) = createRefs()

        var isLiked by remember(key1 = quoteString, key2 = quoteAuthor) {
            mutableStateOf(false)
        }

        Text(
            text = quoteString,
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            fontSize = 20.sp,
        )

        Text(
            text = quoteAuthor,
            modifier = Modifier.constrainAs(author) {
                top.linkTo(text.bottom, 16.dp)
                end.linkTo(parent.end, 16.dp)
            },
            fontSize = 22.sp
        )

        IconButton(
            onClick = {
                isLiked = !isLiked
                if (isLiked) {
                    onDelete()
                }else{
                    onLike()
                }
            },
            modifier = Modifier.constrainAs(heartButton) {
                start.linkTo(parent.start, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            }
        ) {
            Icon(
                imageVector = if (!isLiked) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                contentDescription = "Heart button",
                tint = Color(0xFF07136B)
            )
        }

        IconButton(
            onClick = onShare,
            modifier = Modifier.constrainAs(shareButton) {
                end.linkTo(parent.end, 8.dp)
                bottom.linkTo(parent.bottom, 8.dp)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Share button",
                tint = Color(0xFF07136B)
            )
        }
    }
}