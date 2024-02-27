package com.nikitasutulov.lab2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class OutputFragment : Fragment() {

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_output, container, false)

        setInputFragmentResultListener()
        setCancelButtonOnClickListener()

        return view
    }

    private fun setInputFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener("task", this) { _, bundle ->
            val infoTV = view.findViewById<TextView>(R.id.infoTV)!!
            infoTV.text = """
                Завдання: ${bundle.getString("text")}
                Складність: ${bundle.getString("complexity")}
                Тип: ${bundle.getString("type")}
            """.trimIndent()
        }
    }

    private fun setCancelButtonOnClickListener() {
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)!!
        cancelButton.setOnClickListener {
            val infoTV = view.findViewById<TextView>(R.id.infoTV)!!
            infoTV.text = ""
            parentFragmentManager.setFragmentResult("reset", Bundle())
        }
    }
}