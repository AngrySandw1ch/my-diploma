package ru.netology.mydiploma.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.mydiploma.roomdb.converter.AttachmentTypeConverter
import ru.netology.mydiploma.roomdb.converter.CoordinatesConverter
import ru.netology.mydiploma.roomdb.converter.EventTypeConverter
import ru.netology.mydiploma.roomdb.converter.MainConverter
import ru.netology.mydiploma.roomdb.dao.EventDao
import ru.netology.mydiploma.roomdb.dao.JobDao
import ru.netology.mydiploma.roomdb.dao.PostDao
import ru.netology.mydiploma.roomdb.dao.UserDao
import ru.netology.mydiploma.roomdb.entity.EventEntity
import ru.netology.mydiploma.roomdb.entity.JobEntity
import ru.netology.mydiploma.roomdb.entity.PostEntity
import ru.netology.mydiploma.roomdb.entity.UserEntity


@Database(entities = [PostEntity::class, EventEntity::class, UserEntity::class, JobEntity::class], version = 1, exportSchema = false)
@TypeConverters(CoordinatesConverter::class, MainConverter::class, EventTypeConverter::class, AttachmentTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun jobDao(): JobDao


    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        private fun buildDataBase(context: Context): AppDb {
           return Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}