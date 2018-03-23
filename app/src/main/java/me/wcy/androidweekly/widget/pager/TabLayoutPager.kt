package me.wcy.androidweekly.widget.pager

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.ViewGroup
import me.wcy.androidweekly.R

/**
 * Created by wcy on 2017/11/26.
 */
class TabLayoutPager(private val fragmentManager: FragmentManager, private val fragmentContainer: ViewGroup, private val tabLayout: TabLayout) {
    private val fragmentList = mutableListOf<Fragment>()
    private val titleList = mutableListOf<String>()
    private var position = 0
    private var viewPager: ScrollableViewPager? = null

    fun addFragment(fragment: Fragment, title: String) {
        if (viewPager == null) {
            fragmentList.add(fragment)
            titleList.add(title)
        }
    }

    fun setPosition(position: Int) {
        this.position = position
        if (viewPager != null) {
            tabLayout.setScrollPosition(position, 0f, true)
            viewPager!!.currentItem = position
        }
    }

    fun show() {
        val context = fragmentContainer.context
        val adapter = FragmentAdapter(fragmentManager, fragmentList)
        adapter.setTitleList(titleList)
        viewPager = LayoutInflater.from(context).inflate(R.layout.tab_view_pager, fragmentContainer, false) as ScrollableViewPager
        viewPager!!.offscreenPageLimit = fragmentList.size
        viewPager!!.adapter = adapter
        viewPager!!.currentItem = position
        fragmentContainer.addView(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }
}
