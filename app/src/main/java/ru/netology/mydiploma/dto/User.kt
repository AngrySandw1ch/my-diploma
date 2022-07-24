package ru.netology.mydiploma.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String? = null
) : Parcelable
