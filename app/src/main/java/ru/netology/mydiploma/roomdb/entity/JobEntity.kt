package ru.netology.mydiploma.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.mydiploma.dto.Job

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val position: String,
    val start: Long,
    val finish: Long?,
    val link: String?
) {
    fun toDto() = Job(
        id = id,
        name = name,
        position = position,
        start = start,
        finish = finish,
        link = link
    )

    companion object {
        fun fromDto(job: Job) = JobEntity(
            id = job.id,
            name = job.name,
            position = job.position,
            start = job.start,
            finish = job.finish,
            link = job.link
        )
    }
}

fun List<Job>.toEntity(): List<JobEntity> = map(JobEntity::fromDto)
fun List<JobEntity>.fromEntity(): List<Job> = map(JobEntity::toDto)