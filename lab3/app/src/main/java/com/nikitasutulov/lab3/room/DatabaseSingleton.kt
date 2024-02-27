package com.nikitasutulov.lab3.room

import android.content.Context
import androidx.room.Room

object DatabaseSingleton {

    @Volatile
    private var INSTANCE: TaskDatabase? = null

    fun getInstance(context: Context): TaskDatabase {
        return INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
    }

    private fun buildDatabase(context: Context): TaskDatabase {
        return Room.databaseBuilder(context.applicationContext,
            TaskDatabase::class.java, "task-db")
            .build()
    }
}
