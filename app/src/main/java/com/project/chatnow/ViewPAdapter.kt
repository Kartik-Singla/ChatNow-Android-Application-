package com.project.chatnow

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val TAG = "viewpadapter"
     var fragments:ArrayList<Fragment>
     var titles:ArrayList<String>

    init {
        this.fragments = ArrayList()
        this.titles = ArrayList()
    }
    override fun getItem(position: Int): Fragment { // getting fragments
        Log.d(TAG,"getITem called ${position}")
        return fragments.get(position)
}

    override fun getCount(): Int {
        Log.d(TAG,"getI called")
        return fragments.size
    }

    fun addFragments(fragment: Fragment, title:String){
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? { // getting titles...for example Chats, Users
        Log.d(TAG,"getPageTitle called")
        return titles.get(position)
    }
}