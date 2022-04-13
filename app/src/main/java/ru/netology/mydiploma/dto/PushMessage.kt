package ru.netology.mydiploma.dto

data class PushMessage(
    val recipientId: Long?,
    val content: String,
)