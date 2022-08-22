package ru.netology.mydiploma.roomdb.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.mydiploma.roomdb.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Singleton
    @Provides
    fun provideEventDao(db: AppDb): EventDao {
        return db.eventDao()
    }

    @Singleton
    @Provides
    fun providePostDao(db: AppDb): PostDao {
        return db.postDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDb): UserDao {
        return db.userDao()
    }
}