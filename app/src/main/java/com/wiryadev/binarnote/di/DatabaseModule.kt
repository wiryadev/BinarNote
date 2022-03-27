package com.wiryadev.binarnote.di

import android.content.Context
import androidx.room.Room
import com.wiryadev.binarnote.data.local.db.BinarNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): BinarNoteDatabase = Room
        .databaseBuilder(
            appContext,
            BinarNoteDatabase::class.java,
            "binar_note_db"
        )
        .build()

}