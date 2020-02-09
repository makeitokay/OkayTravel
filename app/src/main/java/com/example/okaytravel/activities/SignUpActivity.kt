package com.example.okaytravel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.SignUpPresenter
import com.example.okaytravel.views.SignUpView
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

class SignUpActivity : MvpAppCompatActivity(), SignUpView {

    @InjectPresenter
    lateinit var signUpPresenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUp.setOnClickListener {
            signUpPresenter.doSignUp()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
