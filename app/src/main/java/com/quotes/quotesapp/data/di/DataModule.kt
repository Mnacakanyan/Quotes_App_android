package com.quotes.quotesapp.data.di

import com.quotes.quotesapp.data.api.KtorClientProvider
import com.quotes.quotesapp.data.repository.QuoteRepositoryImpl
import com.quotes.quotesapp.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideQuoteRepository(httpClient: HttpClient): QuoteRepository {
        return QuoteRepositoryImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return KtorClientProvider.client
    }
}