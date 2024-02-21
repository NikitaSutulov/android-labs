package com.nikitasutulov.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOkButtonOnClickListener()
    }

    private fun setOkButtonOnClickListener() {
        val okButton = findViewById<Button>(R.id.okButton)!!
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
            val infoTV = findViewById<TextView>(R.id.infoTV)!!
            infoTV.text = """
                Завдання: $text
                Складність: $complexity
                Тип: $type
            """.trimIndent()
        }
    }

    private fun getTaskText(): String {
        val taskEditText = findViewById<EditText>(R.id.taskEditText)
        val text = taskEditText.text.toString()
        if (text.isBlank()) {
            Toast.makeText(this, "Будь ласка, введіть текст для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return text
    }

    private fun getTaskComplexity(): String {
        val complexityRG = findViewById<RadioGroup>(R.id.complexityRadioGroup)
        val chosenComplexityRadioId = complexityRG.checkedRadioButtonId
        if (chosenComplexityRadioId == -1) {
            Toast.makeText(this, "Будь ласка, оберіть складність для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return findViewById<RadioButton>(chosenComplexityRadioId)!!.text.toString()
    }

    private fun getTaskType(): String {
        val typeRG = findViewById<RadioGroup>(R.id.typeRadioGroup)
        val chosenTypeRadioId = typeRG.checkedRadioButtonId
        if (chosenTypeRadioId == -1) {
            Toast.makeText(this, "Будь ласка, оберіть тип для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return findViewById<RadioButton>(chosenTypeRadioId)!!.text.toString()
    }

}