package com.quotes.quotesapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    @SerialName("a")
    val author: String,

    @SerialName("q")
    val quote: String,

    val h: String,
)