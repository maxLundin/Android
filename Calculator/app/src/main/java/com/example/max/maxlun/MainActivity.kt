package com.example.max.maxlun

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import java.lang.StringBuilder
import com.example.max.maxlun.expression.parser.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var but1: TextView
    private lateinit var but2: TextView
    private lateinit var but3: TextView
    private lateinit var but4: TextView
    private lateinit var but5: TextView
    private lateinit var but6: TextView
    private lateinit var but7: TextView
    private lateinit var but8: TextView
    private lateinit var but9: TextView
    private lateinit var but0: TextView
    private lateinit var butmul: TextView
    private lateinit var butsub: TextView
    private lateinit var butpercent: TextView
    private lateinit var butadd: TextView
    private lateinit var butdiv: TextView
    private lateinit var butdot: TextView
    private lateinit var butend: TextView
    private lateinit var display: TextView
    private var displyTextSize = 32f
    private lateinit var butC: TextView
    private lateinit var butback: ImageView
    private lateinit var sb: StringBuilder
    private lateinit var butbl: TextView
    private lateinit var butbr: TextView

    private fun checkSize(ch: Char) {
        display.text = sb.append(ch).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.dispay)
        display.movementMethod = ScrollingMovementMethod()

        butC = findViewById(R.id.numC)
        but1 = findViewById(R.id.num1)
        but2 = findViewById(R.id.num2)
        but3 = findViewById(R.id.num3)
        but4 = findViewById(R.id.num4)
        but5 = findViewById(R.id.num5)
        but6 = findViewById(R.id.num6)
        but7 = findViewById(R.id.num7)
        but8 = findViewById(R.id.num8)
        but9 = findViewById(R.id.num9)
        but0 = findViewById(R.id.num0)
        butbr = findViewById(R.id.numbr)
        butbl = findViewById(R.id.numbl)
        butmul = findViewById(R.id.nummultyply)
        butsub = findViewById(R.id.numminis)
        butadd = findViewById(R.id.numplus)
        butdot = findViewById(R.id.numdot)
        butdiv = findViewById(R.id.numdivide)
        butpercent = findViewById(R.id.numpercent)
        butend = findViewById(R.id.numend)
        butback = findViewById(R.id.numback)

        sb = StringBuilder()
        butback.setOnClickListener {
            if (sb.isNotEmpty()) {
                display.text = sb.deleteCharAt(sb.length - 1).toString()
            }
        }
        butC.setOnClickListener {
            sb.setLength(0);
            display.text = "Text"
            displyTextSize = 32f
            display.setTextSize(TypedValue.COMPLEX_UNIT_SP, displyTextSize)
        }
        but1.setOnClickListener { checkSize('1') }
        but2.setOnClickListener { checkSize('2') }
        but3.setOnClickListener { checkSize('3') }
        but4.setOnClickListener { checkSize('4') }
        but5.setOnClickListener { checkSize('5') }
        but6.setOnClickListener { checkSize('6') }
        but7.setOnClickListener { checkSize('7') }
        but8.setOnClickListener { checkSize('8') }
        but9.setOnClickListener { checkSize('9') }
        but0.setOnClickListener { checkSize('0') }
        butmul.setOnClickListener { checkSize('*') }
        butadd.setOnClickListener { checkSize('+') }
        butdiv.setOnClickListener { checkSize('/') }
        butpercent.setOnClickListener { checkSize('%') }
        butdot.setOnClickListener { checkSize(',') }
        butsub.setOnClickListener { checkSize('-') }
        butbr.setOnClickListener { checkSize(')') }
        butbl.setOnClickListener { checkSize('(') }
        butend.setOnClickListener {
            val parser = ExpressionParser()
            try {
                val parse = parser.parse(sb.toString())
                sb = StringBuilder(parse.toString())
                display.text = parse.toString();
            } catch (e: ParserException) {
                display.text = "!&#error"
                sb.setLength(0)
            } catch (e: DivisionByZeroException) {
                display.text = "Division By Zero"
                sb.setLength(0)
            }
        }

        if (savedInstanceState != null) {
            sb = StringBuilder(savedInstanceState.getString(CNT))
            display.text = sb.toString()
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CNT, sb.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "onPause: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(LOG_TAG, "onRestart: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy: ")
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
        private val CNT = MainActivity::class.java.name + ".cnt"
    }
}
