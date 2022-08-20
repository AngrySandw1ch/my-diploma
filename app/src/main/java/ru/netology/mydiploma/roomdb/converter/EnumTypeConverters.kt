package ru.netology.mydiploma.roomdb.converter

import androidx.room.TypeConverter
import ru.netology.mydiploma.dto.Coordinates
import ru.netology.mydiploma.enumeration.AttachmentType
import ru.netology.mydiploma.enumeration.EventType
import java.lang.StringBuilder

class EventTypeConverter {
    @TypeConverter
    fun toEventType(value: String) = enumValueOf<EventType>(value)

    @TypeConverter
    fun fromEventType(value: EventType) = value.name
}

class AttachmentTypeConverter {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}


class CoordinatesConverter {
    @TypeConverter
    fun toCoordinates(value: String): Coordinates {
        val data = value.split(" ")
        return Coordinates().apply {
            lat = data[0].toDouble()
            long = data[1].toDouble()
        }
    }

    @TypeConverter
    fun fromCoordinates(value: Coordinates): String {
        val data = StringBuilder().append(value.lat).append(" ").append(value.long)
        return data.toString()
    }
}