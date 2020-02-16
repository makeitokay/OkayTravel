package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.SyncAnonymUserDialogFragment
import com.example.okaytravel.presenters.SignUpPresenter
import com.example.okaytravel.views.SignUpView
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : MvpAppCompatActivity(), SignUpView, SyncAnonymUserDialogFragment.ChooseListener {

    @ProvidePresenter
    fun provideSignUpPresenter(): SignUpPresenter {
        return SignUpPresenter(this)
    }

    @InjectPresenter
    lateinit var signUpPresenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpBtn.setOnClickListener {
            signUpPresenter.startSignUp(
                username.text.toString(),
                email.text.toString(),
                password.text.toString(),
                passwordAgain.text.toString()
            )
        }
    }

    override fun showSyncAnonymUserDialog() {
        SyncAnonymUserDialogFragment().show(supportFragmentManager, "SyncAnonymUserDialogFragmentTag")
    }

    override fun syncAnonymUserButtonClicked() {
        signUpPresenter.signUpWithSyncAnonym(username.text.toString(), email.text.toString(), password.text.toString())
    }

    override fun cancelSyncAnonymUserButtonClicked() {
        signUpPresenter.signUpWithoutSyncAnonym(username.text.toString(), email.text.toString(), password.text.toString())
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resourceId: Int) {
        showMessage(getString(resourceId))
    }

    override fun startSigningUp() {
        signUpBtn.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    override fun endSigningUp() {
        signUpBtn.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }

    override fun focusUsername() {
        username.requestFocus()
    }

    override fun focusEmail() {
        email.requestFocus()
    }

    override fun focusPassword() {
        password.requestFocus()
    }

    override fun focusPasswordAgain() {
        passwordAgain.requestFocus()
    }

    override fun startLoginView() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }
}
