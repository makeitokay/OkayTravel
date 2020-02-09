package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.LoginPresenter
import com.example.okaytravel.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import javax.xml.transform.Templates


class LoginActivity : MvpAppCompatActivity(), LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        createAccountBtn.setOnClickListener {
            openSignUp()
        }
        loginBtn.setOnClickListener {
            loginPresenter.doLogin()
        }
    }

    override fun openSignUp() {
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
    }

    override fun startSigningIn() {
        loginBtn.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    override fun endSigningIn() {
        loginBtn.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

}