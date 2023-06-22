package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var canAddOperationAction = false

    val workingTV = findViewById<TextView>(R.id.working_textview)
    val resultsTV = findViewById<TextView>(R.id.results_textview)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun allClearAction(view: View) {
        workingTV.text = ""
        resultsTV.text = ""
    }

    fun backspaceAction(view: View) {
        val length = workingTV.length()
        if (length > 0) {
            workingTV.text = workingTV.text.subSequence(0, length - 1)
        }
    }
}