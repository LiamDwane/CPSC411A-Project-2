package com.example.cpsc411project2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}

fun createDatabase(context: Context): AppDatabase{
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "user_database"
    ).build()
}