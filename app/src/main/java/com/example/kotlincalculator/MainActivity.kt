package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.kotlincalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun allClearAction(view: View) {
        binding.workingTextview.text = ""
        binding.resultsTextview.text = ""
    }

    fun backspaceAction(view: View) {
        val length = binding.workingTextview.length()
        if (length > 0) {
            binding.workingTextview.text = binding.workingTextview.text.subSequence(0, length - 1)
        }
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    binding.workingTextview.append(view.text)
                }
                canAddDecimal = false
            } else {
                binding.workingTextview.append(view.text)
            }
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workingTextview.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun equalsAction(view: View) {
        binding.resultsTextview.text = calculateResults()
    }

    private fun calculateResults(): String {

        return ""
    }

    private  fun digitOparators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for(char in binding.workingTextview.text) {
            if (char.isDigit() || char == '.') {
                currentDigit = currentDigit + char
            } else { //jodi operator pai
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(char)
            }
        }

        if (currentDigit != "") { //last operand
            list.add(currentDigit.toFloat())
        }

        return list
    }
}
