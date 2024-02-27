package com.nikitasutulov.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.nikitasutulov.lab3.room.DatabaseSingleton
import com.nikitasutulov.lab3.room.Task
import com.nikitasutulov.lab3.room.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedDataActivity : AppCompatActivity() {

    private val dbDao: TaskDao by lazy {
        DatabaseSingleton.getInstance(applicationContext).taskDao()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_data_actvity)

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                val savedTask: Task? = getTaskFromDb()
                if (savedTask != null) {
                    displayTask(savedTask)
                } else {
                    displayError()
                }
            }
        }
    }

    private fun displayError() {
        val savedDataTV = findViewById<TextView>(R.id.savedDataTV)
        savedDataTV.setText("Error: no saved data in the Room DB")
    }

    private fun getTaskFromDb(): Task? {
        val tasksList = dbDao.getTask()
        return if (tasksList.isEmpty()) {
            null
        } else {
            tasksList[0]
        }
    }

    private fun displayTask(savedTask: Task) {
        val savedDataTV = findViewById<TextView>(R.id.savedDataTV)
        savedDataTV.setText("""
            Завдання: ${savedTask.text}
            Складність: ${savedTask.complexity}
            Тип: ${savedTask.type}
            """.trimIndent()
        )
    }
}