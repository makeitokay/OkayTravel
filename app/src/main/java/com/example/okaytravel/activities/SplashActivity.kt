package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.presenters.SplashPresenter
import com.example.okaytravel.views.SplashView

class SplashActivity : MvpAppCompatActivity(), SplashView {

    @ProvidePresenter
    fun provideSplashPresenter(): SplashPresenter {
        return SplashPresenter(this)
    }

    @InjectPresenter
    lateinit var splashPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashPresenter.checkUserSession()
    }

    override fun startLogin() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    override fun startIntro() {
        startLogin()
        // TODO
    }

    override fun startHome() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }
}