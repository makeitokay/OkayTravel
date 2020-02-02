package com.example.okaytravel.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.okaytravel.MainActivity

import com.example.okaytravel.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        with (intent) {
            putExtra("email", email.text.toString())
            putExtra("password", password.text.toString())
        }
        startActivity(intent)
    }
}