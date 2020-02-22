package com.example.okaytravel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.dialogs.SyncAnonymUserDialogFragment
import com.example.okaytravel.presenters.SignUpPresenter
import com.example.okaytravel.views.SignUpView
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity(), SignUpView, SyncAnonymUserDialogFragment.ChooseListener {

    override val fragmentContainer: Int? = null

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
        SyncAnonymUserDialogFragment()
            .show(supportFragmentManager, "SyncAnonymUserDialogFragmentTag")
    }

    override fun syncAnonymUserButtonClicked() {
        signUpPresenter.endSignUpWithSyncAnonym(username.text.toString(), email.text.toString(), password.text.toString())
    }

    override fun cancelSyncAnonymUserButtonClicked() {
        signUpPresenter.endSignUpWithoutSyncAnonym(username.text.toString(), email.text.toString(), password.text.toString())
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

    override fun startHome() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}
