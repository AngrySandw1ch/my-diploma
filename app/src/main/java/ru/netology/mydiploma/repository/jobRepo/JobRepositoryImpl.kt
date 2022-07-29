package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okio.IOException
import ru.netology.mydiploma.api.JobApi
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError

class JobRepositoryImpl() : JobRepository {

    private val _data: MutableLiveData<List<Job>> = MutableLiveData()
    override val data: LiveData<List<Job>> get() = _data




    override suspend fun getUserJobs(id: Long) {
        try {
            val response = JobApi.service.getUserJobs(id)
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
            val response = JobApi.service.getCurrentUserJobs()
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

    override suspend fun saveJob(job: Job) {
        try {
            val response = JobApi.service.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            _data.postValue(_data.value?.map {
                if (it.id == body.id) body else it
            })

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
            _data.postValue(_data.value?.filter {
                it.id != id
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}