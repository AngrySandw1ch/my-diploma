package ru.netology.mydiploma.dto

import androidx.room.ColumnInfo

class Coordinates() {
    var lat: Double = 0.0
    @ColumnInfo(name = "_long")
    var long: Double = 0.0
}
