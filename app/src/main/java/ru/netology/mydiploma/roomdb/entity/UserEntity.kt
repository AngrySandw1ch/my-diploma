package ru.netology.mydiploma.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.mydiploma.dto.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String?
) {
    fun toDto() = User(
        id = id,
        login = login,
        name = name,
        avatar = avatar
    )

    companion object {
        fun fromDto(dto: User) = UserEntity(
            id = dto.id,
            login = dto.login,
            name = dto.name,
            avatar = dto.avatar
        )
    }
}

fun List<UserEntity>.fromEntity(): List<User> = map(UserEntity::toDto)
fun List<User>.toEntity(): List<UserEntity> = map(UserEntity::fromDto)