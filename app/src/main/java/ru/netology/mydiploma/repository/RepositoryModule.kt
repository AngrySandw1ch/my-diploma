package ru.netology.mydiploma.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.mydiploma.repository.eventRepo.EventRepository
import ru.netology.mydiploma.repository.eventRepo.EventRepositoryImpl
import ru.netology.mydiploma.repository.jobRepo.JobRepository
import ru.netology.mydiploma.repository.jobRepo.JobRepositoryImpl
import ru.netology.mydiploma.repository.postRepo.PostRepository
import ru.netology.mydiploma.repository.postRepo.PostRepositoryImpl
import ru.netology.mydiploma.repository.userRepo.UserRepository
import ru.netology.mydiploma.repository.userRepo.UserRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    @Singleton
    abstract fun bindJobRepository(impl: JobRepositoryImpl): JobRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

}