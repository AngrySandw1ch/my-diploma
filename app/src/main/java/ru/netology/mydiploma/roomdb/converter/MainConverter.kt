package ru.netology.mydiploma.roomdb.converter

import androidx.room.TypeConverter
import java.lang.StringBuilder

class MainConverter {
    @TypeConverter
    fun fromSet(set: Set<Long>) : String {
        val stringBuilder = StringBuilder()
        for (value in set) {
            stringBuilder.append(value).append(" ")
        }
        return stringBuilder.toString().trim()
    }

    @TypeConverter
    fun toSet(data: String): Set<Long> {
        val dataSet = mutableSetOf<Long>()
        val dataList = data.trim().split(" ")

        for (value in dataList) {
            if (value.isBlank()) continue
            dataSet.add(value.toLong())
        }

        return dataSet
    }
}