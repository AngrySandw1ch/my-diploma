package ru.netology.mydiploma.dto

data class User(
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String? = null
)
