package ru.netology.mydiploma.model

data class ModelState(
    val refreshing: Boolean = false,
    val loading: Boolean = false,
    val error: Boolean = false,
)