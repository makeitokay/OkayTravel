package com.example.okaytravel.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.LoginActivity
import com.example.okaytravel.activities.SignUpActivity
import com.example.okaytravel.presenters.ProfilePresenter
import com.example.okaytravel.views.ProfileView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar.*

class ProfileFragment: BaseFragment(), ProfileView {

    override val fragmentNameResource: Int
        get() = R.string.profileMenuItemText

    @ProvidePresenter
    fun provideProfilePresenter(): ProfilePresenter {
        return ProfilePresenter(this.requireActivity())
    }

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePresenter.update()

        openLoginBtn.setOnClickListener {
            openLogin()
        }

        openSignUpBtn.setOnClickListener {
            openSignUp()
        }

        logoutButton.setOnClickListener {
            profilePresenter.logout()
        }
    }

    override fun setUsername(username: String) {
        usernameView.text = username
    }

    override fun openLogin() {
        startActivity(Intent(this.requireActivity(), LoginActivity::class.java))
    }

    override fun openSignUp() {
        startActivity(Intent(this.requireActivity(), SignUpActivity::class.java))
    }

    override fun hideLogoutBtn() {
        logoutButton.visibility = View.GONE
    }

    override fun hideSignInButtons() {
        openLoginBtn.visibility = View.GONE
        openSignUpBtn.visibility = View.GONE
    }

}