package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Job

interface JobRepository {
    val data: LiveData<List<Job>>
    suspend fun getUserJobs(id: Long)
    suspend fun getCurrentUserJobs()
    suspend fun saveJob(job: Job)
    suspend fun removeJob(id: Long)
}