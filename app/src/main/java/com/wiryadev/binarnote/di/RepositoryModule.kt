package com.wiryadev.binarnote.di

import com.wiryadev.binarnote.data.repositories.NoteRepository
import com.wiryadev.binarnote.data.repositories.NoteRepositoryImpl
import com.wiryadev.binarnote.data.repositories.UserRepository
import com.wiryadev.binarnote.data.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        DatabaseModule::class,
        DataStoreModule::class,
    ]
)
abstract class RepositoryModule {

    @Binds
    abstract fun provideNoteRepository(repository: NoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun provideUserRepository(repository: UserRepositoryImpl): UserRepository

}