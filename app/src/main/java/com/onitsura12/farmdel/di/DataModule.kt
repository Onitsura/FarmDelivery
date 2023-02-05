package com.onitsura12.farmdel.di

import com.onitsura12.farmdel.data.datasources.FirebaseDataSource
import com.onitsura12.farmdel.data.repository.UserRepositoryImpl
import com.onitsura12.farmdel.data.storage.FirebaseStorage
import com.onitsura12.farmdel.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {


    @Provides
    @Singleton
    fun provideDataSource(): FirebaseDataSource{
        return FirebaseDataSource()
    }

    @Provides
    @Singleton
    fun provideUserStorage(dataSource: FirebaseDataSource): FirebaseStorage{
        return FirebaseStorage(firebaseDataSource = dataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(storage: FirebaseStorage): UserRepository {
        return UserRepositoryImpl(storage = storage)
    }

}




