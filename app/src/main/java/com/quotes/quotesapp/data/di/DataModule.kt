package com.quotes.quotesapp.data.di

import android.content.Context
import androidx.room.Room
import com.quotes.quotesapp.data.api.KtorClientProvider
import com.quotes.quotesapp.data.db.QuoteDao
import com.quotes.quotesapp.data.db.QuoteDatabase
import com.quotes.quotesapp.data.repository.FavoritesRepositoryImpl
import com.quotes.quotesapp.data.repository.QuoteRepositoryImpl
import com.quotes.quotesapp.domain.repository.FavoritesRepository
import com.quotes.quotesapp.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideFavoritesRepository(quoteDao: QuoteDao): FavoritesRepository {
        return FavoritesRepositoryImpl(quoteDao)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return KtorClientProvider.client
    }

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(
            context,
            QuoteDatabase::class.java,
            "quote_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(quoteDatabase: QuoteDatabase): QuoteDao {
        return quoteDatabase.getQuoteDao()
    }

}