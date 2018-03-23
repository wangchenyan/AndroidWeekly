package me.wcy.androidweekly.widget.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>) : FragmentPagerAdapter(fm) {
    private var titleList: List<String>? = null

    fun setTitleList(titleList: List<String>) {
        this.titleList = titleList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (titleList != null) {
            return titleList!![position]
        }
        return super.getPageTitle(position)
    }
}
