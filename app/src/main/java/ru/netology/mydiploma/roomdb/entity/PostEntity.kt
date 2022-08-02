package ru.netology.mydiploma.roomdb.entity

import androidx.room.*
import ru.netology.mydiploma.dto.Attachment
import ru.netology.mydiploma.dto.Coordinates
import ru.netology.mydiploma.dto.Post
import ru.netology.mydiploma.enumeration.AttachmentType
import ru.netology.mydiploma.roomdb.converter.MainConverter

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    @Embedded
    val coords: Coordinates?,
    val link: String?,
    val mentionIds: Set<Long>,
    val mentionedMe: Boolean,
    val likeOwnerIds: Set<Long>,
    val likedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?
    ) {

    fun toDto() = Post(
       id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        coords,
        link,
        mentionIds,
        mentionedMe,
        likeOwnerIds,
        likedByMe,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            id = dto.id,
            authorId = dto.authorId,
            author = dto.author,
            authorAvatar = dto.authorAvatar,
            content = dto.content,
            published = dto.published,
            coords = dto.coords,
            link = dto.link,
            mentionIds = dto.mentionIds,
            mentionedMe = dto.mentionedMe,
            likeOwnerIds = dto.likeOwnerIds,
            likedByMe = dto.likedByMe,
            AttachmentEmbeddable.fromDto(dto.attachment)
        )
    }
}

data class AttachmentEmbeddable(
    val url: String,
    val type: AttachmentType) {

    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)