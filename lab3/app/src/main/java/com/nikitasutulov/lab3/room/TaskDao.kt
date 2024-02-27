package com.nikitasutulov.lab3.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task)

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Query("SELECT * FROM task")
    fun getTask() : List<Task>
}