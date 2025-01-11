package com.iyke.onlinebanking.di

import android.content.Context
import androidx.room.Room
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.data.local.database.AppDatabase
import com.iyke.onlinebanking.data.local.database.MIGRATION_1_2
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
    fun providersStatementDao(appDatabase: AppDatabase) {
        appDatabase.getBankStatementDao()
    }


}