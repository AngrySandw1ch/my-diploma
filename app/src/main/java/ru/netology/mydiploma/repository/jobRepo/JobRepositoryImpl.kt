package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import okio.IOException
import ru.netology.mydiploma.api.JobApi
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError
import ru.netology.mydiploma.roomdb.AppDb
import ru.netology.mydiploma.roomdb.dao.JobDao
import ru.netology.mydiploma.roomdb.entity.JobEntity
import ru.netology.mydiploma.roomdb.entity.toEntity

class JobRepositoryImpl(private val dao: JobDao) : JobRepository {

    override val data: LiveData<List<Job>> = dao.getJobs().map { jobsEntityList ->
        jobsEntityList.map { jobEntity ->
            jobEntity.toDto()
        }
    }


    override suspend fun getUserJobs(id: Long) {
        try {
            val response = JobApi.service.getUserJobs(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getCurrentUserJobs() {
        try {
            val response = JobApi.service.getCurrentUserJobs()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveJob(job: Job) {
        try {
            val response = JobApi.service.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(JobEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeJob(id: Long) {
        try {
            val response = JobApi.service.removeJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}