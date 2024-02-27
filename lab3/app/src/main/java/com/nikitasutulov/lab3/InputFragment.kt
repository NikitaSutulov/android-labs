package com.nikitasutulov.lab3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.nikitasutulov.lab3.room.DatabaseSingleton
import com.nikitasutulov.lab3.room.Task
import com.nikitasutulov.lab3.room.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InputFragment : Fragment() {

    private lateinit var view: View
    private val dbDao: TaskDao by lazy {
        DatabaseSingleton.getInstance(requireContext().applicationContext).taskDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_input, container, false)

        setOkButtonOnClickListener()
        setOpenButtonOnClickListener()
        setClearButtonOnClickListener()
        setOutputFragmentResultListener()

        return view
    }

    private fun setOpenButtonOnClickListener() {
        val openButton = view.findViewById<Button>(R.id.openButton)!!
        openButton.setOnClickListener {
            val intent = Intent(activity, SavedDataActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setClearButtonOnClickListener() {
        val clearButton = view.findViewById<Button>(R.id.clearButton)
        clearButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    deleteTask()
                }
            }
            Toast.makeText(context, "Db successfully cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOkButtonOnClickListener() {
        val okButton = view.findViewById<Button>(R.id.okButton)!!
        okButton.setOnClickListener {
            var text: String
            var complexity: String
            var type: String
            try {
                text = getTaskText()
                complexity = getTaskComplexity()
                type = getTaskType()
            } catch (e: IllegalStateException) {
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    saveTask(Task(1, text, complexity, type))
                }
            }
            Toast.makeText(context, "Task successfully saved to db", Toast.LENGTH_SHORT).show()


            val result = Bundle()
            result.putString("text", text)
            result.putString("complexity", complexity)
            result.putString("type", type)
            parentFragmentManager.setFragmentResult("task", result)
        }
    }

    private fun getTaskText(): String {
        val taskEditText = view.findViewById<EditText>(R.id.taskEditText)
        val text = taskEditText.text.toString()
        if (text.isBlank()) {
            Toast.makeText(context, "Будь ласка, введіть текст для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return text
    }

    private fun getTaskComplexity(): String {
        val complexityRG = view.findViewById<RadioGroup>(R.id.complexityRadioGroup)
        val chosenComplexityRadioId = complexityRG.checkedRadioButtonId
        if (chosenComplexityRadioId == -1) {
            Toast.makeText(context, "Будь ласка, оберіть складність для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return view.findViewById<RadioButton>(chosenComplexityRadioId)!!.text.toString()
    }

    private fun getTaskType(): String {
        val typeRG = view.findViewById<RadioGroup>(R.id.typeRadioGroup)
        val chosenTypeRadioId = typeRG.checkedRadioButtonId
        if (chosenTypeRadioId == -1) {
            Toast.makeText(context, "Будь ласка, оберіть тип для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return view.findViewById<RadioButton>(chosenTypeRadioId)!!.text.toString()
    }

    private fun setOutputFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener("reset", this) { _, _ ->
            val taskEditText = view.findViewById<EditText>(R.id.taskEditText)
            val complexityRG = view.findViewById<RadioGroup>(R.id.complexityRadioGroup)
            val typeRG = view.findViewById<RadioGroup>(R.id.typeRadioGroup)

            taskEditText.setText("")
            complexityRG.clearCheck()
            typeRG.clearCheck()
        }
    }

    private fun saveTask(task: Task) {
        dbDao.deleteAllTasks()
        dbDao.insertTask(task)
    }

    private fun deleteTask() {
        dbDao.deleteAllTasks()
    }
}