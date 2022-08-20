package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import androidx.lifecycle.map
import okio.IOException
import ru.netology.mydiploma.api.JobApi
import ru.netology.mydiploma.dto.Job
import ru.netology.mydiploma.error.ApiError
import ru.netology.mydiploma.error.NetworkError
import ru.netology.mydiploma.error.UnknownError
//import ru.netology.mydiploma.roomdb.dao.JobDao
//import ru.netology.mydiploma.roomdb.entity.JobEntity
//import ru.netology.mydiploma.roomdb.entity.fromEntity
//import ru.netology.mydiploma.roomdb.entity.toEntity

class JobRepositoryImpl(private val userId: Long) : JobRepository {

    private val _data = MutableLiveData<List<Job>>()

    override val data: LiveData<List<Job>> = _data

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUserJobs(userId)
        }
    }


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
            val response = JobApi.service.removeJob(id)
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