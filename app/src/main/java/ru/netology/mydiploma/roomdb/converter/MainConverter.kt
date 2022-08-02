package ru.netology.mydiploma.roomdb.converter

import androidx.room.TypeConverter
import java.lang.StringBuilder

class MainConverter {
    @TypeConverter
    fun fromSet(set: Set<Long>) : String {
        val stringBuilder = StringBuilder()
        for (value in set.withIndex()) {
            stringBuilder.append(value).append(" ")
        }
        return stringBuilder.toString().trim()
    }

    @TypeConverter
    fun toSet(data: String): Set<Long> {
        val dataSet = mutableSetOf<Long>()
        val dataList = data.trim().split(" ")
        for (value in dataList) {
            val chars: CharArray = value.toCharArray()
            for (char in chars) {
                if (char.isDigit()) {
                    dataSet.add(char.code.toLong())
                }
            }
        }
        return dataSet
    }
}