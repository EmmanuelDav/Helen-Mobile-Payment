package com.iyke.onlinebanking.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.data.local.database.AppDatabase
import com.iyke.onlinebanking.data.local.database.MIGRATION_1_2
import com.iyke.onlinebanking.data.remote.FirebaseService
import com.iyke.onlinebanking.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providerUserDao(appDatabase: AppDatabase) = appDatabase.getUsersDao()

    @Provides
    @Singleton
    fun providersStatementDao(appDatabase: AppDatabase) = appDatabase.getBankStatementDao()


    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providerFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun providerAuthRepository(
        firebaseAuth: FirebaseAuth,
        usersDao: UsersDao,
        firebaseFirestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepository(firebaseFirestore, firebaseAuth, usersDao)
    }
}