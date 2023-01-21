package com.onitsura12.farmdel.di

import com.onitsura12.data.Mapper
import com.onitsura12.data.UserStorage
import com.onitsura12.data.repository.UserRepositoryImpl
import com.onitsura12.data.storage.firebase.FirebaseUserStorage
import com.onitsura12.domain.repository.UserRepository
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
    fun provideMapper(): Mapper {
        return Mapper()
    }

//    @Provides
//    @Singleton
//    fun provideDao(appDatabase: AppDatabase): UserDao {
//        return appDatabase.UserDao()
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, "user_database")
//            .build()
//    }

//    @Provides
//    @Singleton
//    fun provideSharedPrefsUserStorage(@ApplicationContext context: Context):
//            UserStorage {
//        return SharedPrefsUserStorage(context = context)
//    }

//    @Provides
//    @Singleton
//    fun provideFirebase(){
//        FirebaseHelper.initFirebase()
//    }

    @Provides
    @Singleton
    fun provideFirebaseUserStorage(): UserStorage{
        return FirebaseUserStorage()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userStorage: UserStorage): UserRepository {
        return UserRepositoryImpl(userStorage)
    }


}




