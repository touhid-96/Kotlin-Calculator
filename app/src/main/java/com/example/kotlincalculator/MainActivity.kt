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
        val digitsOperators = digitOparators()
        if (digitsOperators.isEmpty()) return ""

        //BODMAS - times and division calculate before any addition or substraction
        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        //finally handle addition and substraction
        var result = addSubstractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if (operator == '+') result += nextDigit
                if (operator == '-') result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('×') || list.contains('÷')) {
            list = calcTimesDiv(list)  //this function do one times/division at a time
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        var newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float

                when(operator) {
                    '×' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '÷' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else -> { //found addition(+) or substraction(-)
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) {
                newList.add(passedList[i])
            }
        }

        return newList
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
