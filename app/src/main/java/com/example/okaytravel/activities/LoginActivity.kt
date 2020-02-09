package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
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
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        createAccount.setOnClickListener {
            openSignUp()
        }
    }

    override fun openSignUp() {
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
    }
}