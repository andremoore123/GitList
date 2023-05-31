package com.andre.gitlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andre.gitlist.ui.FollowersFragment
import com.andre.gitlist.ui.FollowingFragment

class ViewPagerAdapter(activity: FragmentActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowingFragment.newInstance(username)
            1 -> FollowersFragment.newInstance(username)
            else -> FollowingFragment.newInstance(username)
        }
    }
}