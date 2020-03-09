package com.example.okaytravel.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.okaytravel.R
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage

class IntroActivity : AppIntro2() {

    private val bgColor = Color.parseColor("#5EABD1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPlacesSliderPage()
        initBudgetSliderPage()
        initThingsSliderPage()

        showSkipButton(true)
        setDepthAnimation()

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        startHome()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        startHome()
    }

    private fun startHome() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra("from", "IntroActivity")
        startActivity(intent)
    }

    private fun initPlacesSliderPage() {
        val sliderPage = SliderPage()
        sliderPage.title = getString(R.string.introTitle1)
        sliderPage.description = getString(R.string.introDesc1)
        sliderPage.bgColor = bgColor
        addSlide(AppIntroFragment.newInstance(sliderPage))
    }

    private fun initBudgetSliderPage() {
        val sliderPage = SliderPage()
        sliderPage.title = getString(R.string.introTitle2)
        sliderPage.description = getString(R.string.introDesc2)
        sliderPage.bgColor = bgColor
        addSlide(AppIntroFragment.newInstance(sliderPage))
    }

    private fun initThingsSliderPage() {
        val sliderPage = SliderPage()
        sliderPage.title = getString(R.string.introTitle3)
        sliderPage.description = getString(R.string.introDesc3)
        sliderPage.bgColor = bgColor
        addSlide(AppIntroFragment.newInstance(sliderPage))
    }
}
