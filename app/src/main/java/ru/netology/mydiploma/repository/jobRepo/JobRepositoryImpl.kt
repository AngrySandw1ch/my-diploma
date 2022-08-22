package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okio.IOException
import ru.netology.mydiploma.api.JobApiService
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val jobApiService: JobApiService
    ) : JobRepository {

    private val _data = MutableLiveData<List<Job>>()

    override val data: LiveData<List<Job>> = _data

    override suspend fun getUserJobs(id: Long) {
        try {
            val response = jobApiService.getUserJobs(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(body)

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getCurrentUserJobs() {
        try {
            val response = jobApiService.getCurrentUserJobs()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(body)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveJob(job: Job, userId: Long) {
        try {
            val response = jobApiService.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(_data.value?.map {
                if (it.id != body.id) it else body.copy(ownerId = userId)
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeJob(id: Long) {
        try {
            val response = jobApiService.removeJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            _data.postValue(_data.value?.filter {
                it.id == id
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}