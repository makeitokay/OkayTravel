package com.example.okaytravel.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TripFragmentViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments: MutableList<Fragment> = mutableListOf()
    private val fragmentTitles: MutableList<String> = mutableListOf()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }
}