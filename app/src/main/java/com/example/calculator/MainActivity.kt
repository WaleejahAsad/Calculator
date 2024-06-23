package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import android.view.View
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var btnOne: AppCompatButton
    private lateinit var btnTwo: AppCompatButton
    private lateinit var btnThree: AppCompatButton
    private lateinit var btnFour: AppCompatButton
    private lateinit var btnFive: AppCompatButton
    private lateinit var btnSix: AppCompatButton
    private lateinit var btnSeven: AppCompatButton
    private lateinit var btnEight: AppCompatButton
    private lateinit var btnNine: AppCompatButton
    private lateinit var btnZero: AppCompatButton
    private lateinit var btnAdd: AppCompatButton
    private lateinit var btnSub: AppCompatButton
    private lateinit var btnMultiply: AppCompatButton
    private lateinit var btnDivide: AppCompatButton
    private lateinit var btnOpenBracket: AppCompatButton
    private lateinit var btnDecimal: AppCompatButton
    private lateinit var btnEqual: AppCompatButton
    private lateinit var btnAC: AppCompatButton
    private lateinit var btnClearOneDigit: AppCompatButton
    private lateinit var btnCloseBracket: AppCompatButton
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false
    private var openBrackets = 0

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons
        btnOne = findViewById(R.id.one_btn)
        btnTwo = findViewById(R.id.two_btn)
        btnThree = findViewById(R.id.three_btn)
        btnFour = findViewById(R.id.four_btn)
        btnFive = findViewById(R.id.five_btn)
        btnSix = findViewById(R.id.six_btn)
        btnSeven = findViewById(R.id.seven_btn)
        btnEight = findViewById(R.id.eight_btn)
        btnNine = findViewById(R.id.nine_btn)
        btnZero = findViewById(R.id.zero_btn)
        btnAdd = findViewById(R.id.add_btn)
        btnSub = findViewById(R.id.sub_btn)
        btnMultiply = findViewById(R.id.multiply_btn)
        btnDivide = findViewById(R.id.divide_btn)
        btnOpenBracket = findViewById(R.id.open_bracket_btn)
        btnDecimal = findViewById(R.id.decimalPoint_btn)
        btnEqual = findViewById(R.id.equal_btn)
        btnAC = findViewById(R.id.ac_btn)
        btnClearOneDigit = findViewById(R.id.clear_one_digit)
        btnCloseBracket = findViewById(R.id.close_bracket_btn)

        // Initialize EditText and TextView
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)


        // Set onClickListeners for buttons
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        btnOne.setOnClickListener { onNumberClick(it) }
        btnTwo.setOnClickListener { onNumberClick(it) }
        btnThree.setOnClickListener { onNumberClick(it) }
        btnFour.setOnClickListener { onNumberClick(it) }
        btnFive.setOnClickListener { onNumberClick(it) }
        btnSix.setOnClickListener { onNumberClick(it) }
        btnSeven.setOnClickListener { onNumberClick(it) }
        btnEight.setOnClickListener { onNumberClick(it) }
        btnNine.setOnClickListener { onNumberClick(it) }
        btnZero.setOnClickListener { onNumberClick(it) }

        btnAdd.setOnClickListener { onOperatorClick(it) }
        btnSub.setOnClickListener { onOperatorClick(it) }
        btnMultiply.setOnClickListener { onOperatorClick(it) }
        btnDivide.setOnClickListener { onOperatorClick(it) }

        btnOpenBracket.setOnClickListener { onOpenBracketClick() }
        btnCloseBracket.setOnClickListener { onCloseBracketClick() }

        btnDecimal.setOnClickListener { onDecimalClick() }
        btnEqual.setOnClickListener { onEqualClick() }
        btnAC.setOnClickListener { onACClick() }
        btnClearOneDigit.setOnClickListener { onClearOneDigitClick() }
    }

    private fun onNumberClick(view: View) {
        val button = view as AppCompatButton
        val number = button.text.toString()

        if (stateError) {
            textView1.text = button.text
            stateError = false
        } else {
            textView1.append(number)
        }
        lastNumeric = true
        onEqual()
    }

    private fun onEqualClick() {
        onEqual()
        textView1.text = textView2.text.toString().drop(1)
        textView2.visibility = View.INVISIBLE
    }

    private fun onACClick() {
        textView1.text = ""
        textView2.text = ""
        textView2.visibility = View.INVISIBLE
        stateError = false
        lastDot = false
        lastNumeric = false
        openBrackets = 0
    }

    private fun onClearOneDigitClick() {
        val text = textView1.text.toString()
        if (text.isNotEmpty()) {
            if (text.last() == '(') {
                openBrackets--
            } else if (text.last() == ')') {
                openBrackets++
            }
            textView1.text = text.dropLast(1)
            try {
                val lastChar = textView1.text.lastOrNull()
                if (lastChar != null && lastChar.isDigit()) {
                    onEqual()
                } else {
                    textView2.text = ""
                    textView2.visibility = View.INVISIBLE
                }
            } catch (e: Exception) {
                textView2.text = ""
                textView2.visibility = View.INVISIBLE
                Log.e("Last char Error ", e.toString())
            }
        }
    }


    private fun onOperatorClick(view: View) {
        val button = view as AppCompatButton
        val operator = button.text.toString()

        if (!stateError && lastNumeric) {
            textView1.append(operator)
            lastDot = false
            lastNumeric = false
//            onEqual()
        }
    }

    private fun onDecimalClick() {
        if (lastNumeric && !stateError && !lastDot) {
            textView1.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    private fun onOpenBracketClick() {
        if (!stateError && (!lastNumeric || textView1.text.isEmpty())) {
            textView1.append("(")
            openBrackets++
            lastNumeric = false
            lastDot = false
        }
    }

    private fun onCloseBracketClick() {
        if (openBrackets > 0 && lastNumeric) {
            textView1.append(")")
            openBrackets--
            lastNumeric = false
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = textView1.text.toString()

            try {
                expression = ExpressionBuilder(text).build()
                val result = expression.evaluate()
                textView2.visibility = View.VISIBLE
                textView2.text = getString(R.string.result_format ,result )

            } catch (ex: ArithmeticException) {
                Log.e("Evaluate Error : ", ex.toString())
                textView2.text = getString(R.string.error_message)
                stateError = true
                lastNumeric = false
            } catch (ex: Exception) {
                Log.e("General Error : ", ex.toString())
                textView2.text = getString(R.string.error_message)
                stateError = true
                lastNumeric = false
            }
        }
    }
}
