package com.quotes.quotesapp.domain.di

import com.quotes.quotesapp.domain.repository.QuoteRepository
import com.quotes.quotesapp.domain.usecase.GetQuoteUseCase
import com.quotes.quotesapp.domain.usecase.GetQuotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGetQuoteUseCase(quoteRepository: QuoteRepository): GetQuoteUseCase {
        return GetQuoteUseCase(quoteRepository)
    }

    @Provides
    @Singleton
    fun provideGetQuotesUseCase(quoteRepository: QuoteRepository): GetQuotesUseCase {
        return GetQuotesUseCase(quoteRepository)
    }
}