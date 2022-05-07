package ru.netology.mydiploma.error

import okio.IOException
import java.sql.SQLException


sealed class AppError(val code: String): RuntimeException() {
    companion object {
        fun from(t: Throwable) = when(t) {
            is AppError -> t
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(val status: Int, code: String): AppError(code)
object NetworkError : AppError("error_network")
object DbError : AppError("error_network")
object UnknownError: AppError("error_unknown")