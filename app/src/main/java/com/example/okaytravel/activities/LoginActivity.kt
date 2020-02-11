package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.LoginPresenter
import com.example.okaytravel.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : MvpAppCompatActivity(), LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        createAccountBtn.setOnClickListener {
            openSignUp()
        }
        loginBtn.setOnClickListener {
            loginPresenter.doLogin(login.text.toString(), password.text.toString())
        }
    }

    override fun openSignUp() {
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
    }

    override fun openMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    override fun startSigningIn() {
        loginBtn.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    override fun endSigningIn() {
        loginBtn.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

}