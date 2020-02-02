package com.example.okaytravel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var emailData: String? = intent.getStringExtra("email")
        var password: String? = intent.getStringExtra("password")

        toast(emailData, password)
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

    fun toast(email: String?, password: String?) {
        Toast.makeText(
            this,
            "Nice to meet you, ${email ?: return}. Nice password ${password ?: return}",
            Toast.LENGTH_LONG)
            .show()
    }
}
