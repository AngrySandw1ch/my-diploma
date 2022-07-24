package ru.netology.mydiploma.repository.jobRepo

import androidx.lifecycle.LiveData
import ru.netology.mydiploma.dto.Job

interface JobRepository {
    val data: LiveData<List<Job>>
    suspend fun getJobs(id: Long)
    suspend fun saveJob(job: Job)
    suspend fun removeJob(id: Long)
}