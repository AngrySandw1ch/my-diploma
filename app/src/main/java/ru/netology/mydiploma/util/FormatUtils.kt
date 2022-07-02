package ru.netology.mydiploma.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object FormatUtils {
    private const val THOU = 1000;
    private const val MILL = 1000000;

    fun formatNum(value: Int): String {
        if (value < THOU) return value.toString();
        if (value < MILL) return makeDecimal(value, THOU, "k");
        return makeDecimal(value, MILL, "m");
    }

    private fun makeDecimal(value: Int, div: Int, suffix: String): String {
        val vl = value / (div / 10)
        val whole = vl / 10
        val tenths = vl % 10
        if ((tenths == 0) || (whole >= 10)) {
            return String.format("%d%s", whole, suffix)
        }
        return String.format("%d.%d%s", whole, tenths, suffix)
    }

    fun formatDate(date: String): String {
        val instant = Instant.parse(date)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd hh:mm")
            .format(dateTime)
    }

    fun formatDate(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd hh:mm")
            .format(dateTime)
    }

    fun formatJustDate(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .format(dateTime)
    }

    fun formatJustTime(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        return DateTimeFormatter
            .ofPattern("hh:mm")
            .format(dateTime)
    }

 }