package com.example.okaytravel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLabelText()
    }

    fun saveText(view: View) {
        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("textData", textData.text.toString())
            commit()
        }
        setLabelText()
    }

    fun setLabelText() {
        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE) ?: return
        val savedText = sharedPref.getString("textData", "")
        savedTextView.text = savedText
    }
}
