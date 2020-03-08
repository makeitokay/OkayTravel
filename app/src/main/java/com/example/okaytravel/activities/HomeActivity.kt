package com.example.okaytravel.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.okaytravel.R
import com.example.okaytravel.activities.fragments.BaseFragment
import com.example.okaytravel.activities.fragments.TripsFragment
import com.example.okaytravel.presenters.HomePresenter
import com.example.okaytravel.views.HomeView
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_sign_up_recommend.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.profile_navigation_drawer_header.*


// TODO: добавить прелоадеры: загрузка поездок, добавление поездки, загрузка мест
class HomeActivity : BaseActivity(), HomeView {

    @ProvidePresenter
    fun provideHomePresenter(): HomePresenter {
        return HomePresenter(this)
    }

    @InjectPresenter
    lateinit var homePresenter: HomePresenter

    override val fragmentContainer = R.id.fragment_container

    private fun getFromActivity(): String? {
        return intent.getStringExtra("from")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        MapKitFactory.setApiKey(getString(com.example.okaytravel.R.string.mapkitAccessToken))
        MapKitFactory.initialize(this)

        loadFragment(TripsFragment() as BaseFragment)

        setToolbarHamburgerButton()
        val drawerToggle = ActionBarDrawerToggle(
            this,
            homeContainer,
            findViewById(R.id.toolbar),
            R.string.openNavigationDrawer,
            R.string.closeNavigationDrawer
        )
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navSignIn -> startLogin()
                R.id.navSignUp -> startSignUp()
                R.id.navLogout -> homePresenter.logout()
                R.id.buyPremium -> homePresenter.buyPremium()
            }
            true
        }

        homePresenter.checkUserSession(getFromActivity())
        navigationView.post {
            homePresenter.initProfile()
        }
    }

    override fun closeDrawer() {
        homeContainer.closeDrawers()
    }

    override fun startLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun startSignUp() {
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun hidePremiumBuyMenuItem() {
        navigationView.menu.findItem(R.id.buyPremium).isVisible = false
    }

    override fun hideAnonymousMenuItems() {
        val navigationMenu = navigationView.menu
        navigationMenu.findItem(R.id.navSignIn).isVisible = false
        navigationMenu.findItem(R.id.navSignUp).isVisible = false
    }

    override fun hideAuthorizedMenuItems() {
        val navigationMenu = navigationView.menu
        navigationMenu.findItem(R.id.navLogout).isVisible = false
        navigationMenu.findItem(R.id.buyPremium).isVisible = false
    }

    override fun initProfile(username: String) {
        usernameView.text = username
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun showSignUpRecommendDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_sign_up_recommend, null)
        builder.setView(view)
        val dialog = builder.create()
        view.okButton.setOnClickListener {
            dialog.dismiss()
            homePresenter.endSignUpRecommendShowDialog(view.showNoMoreCheck.isChecked)
        }
        dialog.show()
    }

}
