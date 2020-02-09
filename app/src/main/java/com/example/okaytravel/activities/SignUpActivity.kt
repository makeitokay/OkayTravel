package com.example.okaytravel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.SignUpPresenter
import com.example.okaytravel.views.SignUpView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.loading

class SignUpActivity : MvpAppCompatActivity(), SignUpView {

    @InjectPresenter
    lateinit var signUpPresenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpBtn.setOnClickListener {
            signUpPresenter.doSignUp()
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun startSigningUp() {
        signUpBtn.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    override fun endSigningUp() {
        signUpBtn.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }
}
