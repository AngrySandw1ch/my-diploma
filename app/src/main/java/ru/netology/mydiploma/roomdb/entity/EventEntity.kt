package ru.netology.mydiploma.roomdb.entity

import androidx.room.*
import ru.netology.mydiploma.dto.Coordinates
import ru.netology.mydiploma.dto.Event
import ru.netology.mydiploma.enumeration.EventType
import ru.netology.mydiploma.roomdb.converter.EventTypeConverter
import ru.netology.mydiploma.roomdb.converter.MainConverter

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String?,
    val published: String?,
    @Embedded
    val coords: Coordinates?,
    @TypeConverters(EventTypeConverter::class)
    @ColumnInfo(name = "_type")
    val type: EventType,
    val likeOwnerIds: Set<Long>,
    val likedByMe: Boolean,
    val speakerIds: Set<Long>,
    val participantsIds: Set<Long>,
    val participatedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val link: String?
) {
    fun toDto() = Event(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        datetime,
        published,
        coords,
        type,
        likeOwnerIds,
        likedByMe,
        speakerIds,
        participantsIds,
        participatedByMe,
        attachment?.toDto(),
        link
    )

    companion object {
        fun fromDto(dto: Event) = EventEntity(
            id = dto.id,
            authorId = dto.authorId,
            author = dto.author,
            authorAvatar = dto.authorAvatar,
            content = dto.content,
            datetime = dto.datetime,
            published = dto.published,
            coords = dto.coords,
            type = dto.type,
            likeOwnerIds = dto.likeOwnerIds,
            likedByMe = dto.likedByMe,
            speakerIds = dto.speakerIds,
            participantsIds = dto.participantsIds,
            participatedByMe = dto.participatedByMe,
            AttachmentEmbeddable.fromDto(dto.attachment),
            link = dto.link
        )
    }
}

fun List<Event>.toEntity(): List<EventEntity> = map(EventEntity::fromDto)
fun List<EventEntity>.fromEntity(): List<Event> = map(EventEntity::toDto)
