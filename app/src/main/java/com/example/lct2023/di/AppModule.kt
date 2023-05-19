package com.example.lct2023.di

import android.content.Context
import com.example.lct2023.LctDataStore
import com.example.lct2023.gate.Gate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): LctDataStore = LctDataStore(app)

    @Singleton
    @Provides
    fun provideGate(): Gate = Gate()

}