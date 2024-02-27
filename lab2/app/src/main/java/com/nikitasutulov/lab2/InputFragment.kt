package com.nikitasutulov.lab2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class InputFragment : Fragment() {

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_input, container, false)

        setOkButtonOnClickListener()
        setOutputFragmentResultListener()

        return view
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
            Toast.makeText(this.context, "Будь ласка, введіть текст для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return text
    }

    private fun getTaskComplexity(): String {
        val complexityRG = view.findViewById<RadioGroup>(R.id.complexityRadioGroup)
        val chosenComplexityRadioId = complexityRG.checkedRadioButtonId
        if (chosenComplexityRadioId == -1) {
            Toast.makeText(this.context, "Будь ласка, оберіть складність для Вашого завдання", Toast.LENGTH_SHORT).show()
            throw IllegalStateException()
        }
        return view.findViewById<RadioButton>(chosenComplexityRadioId)!!.text.toString()
    }

    private fun getTaskType(): String {
        val typeRG = view.findViewById<RadioGroup>(R.id.typeRadioGroup)
        val chosenTypeRadioId = typeRG.checkedRadioButtonId
        if (chosenTypeRadioId == -1) {
            Toast.makeText(this.context, "Будь ласка, оберіть тип для Вашого завдання", Toast.LENGTH_SHORT).show()
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
}