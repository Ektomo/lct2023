package com.example.lct2023.di

import android.app.Application
import android.content.Context
import com.example.lct2023.LctDataStore
import com.example.lct2023.gate.Gate
import com.example.lct2023.gate.LctGate
import com.example.lct2023.util.VoiceRecognizer
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
    fun provideVoiceRecognitionApp(
        @ApplicationContext app: Context
    ): VoiceRecognizer = VoiceRecognizer(app)

    @Singleton
    @Provides
    fun provideGate(): LctGate = LctGate()



}