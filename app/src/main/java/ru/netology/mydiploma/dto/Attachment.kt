package ru.netology.mydiploma.dto

import ru.netology.mydiploma.enumeration.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType,
)
