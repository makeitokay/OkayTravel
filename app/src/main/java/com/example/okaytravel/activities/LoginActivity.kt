package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.presenters.LoginPresenter
import com.example.okaytravel.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), LoginView {

    override val fragmentContainer: Int? = null

    @ProvidePresenter
    fun provideLoginPresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

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
            loginPresenter.Login(login.text.toString(), password.text.toString())
        }
        loginAsAnonymousBtn.setOnClickListener {
            loginPresenter.loginAsAnonymous()
        }
    }

    override fun openSignUp() {
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
    }

    override fun openHome() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
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