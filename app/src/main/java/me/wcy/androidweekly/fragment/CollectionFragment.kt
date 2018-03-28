package me.wcy.androidweekly.fragment

import android.support.design.widget.TabLayout
import android.widget.FrameLayout
import me.wcy.androidweekly.R
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.widget.pager.TabLayoutPager

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
class CollectionFragment : BaseNaviFragment() {
    @Bind(R.id.tab_layout)
    private val tabLayout: TabLayout? = null
    @Bind(R.id.fragment_container)
    private val fragmentContainer: FrameLayout? = null

    override fun layoutResId(): Int {
        return R.layout.fragment_collection
    }

    override fun navigationMenuId(): Int {
        return R.id.action_collection
    }

    override fun onLazyCreate() {
        val tabLayoutPager = TabLayoutPager(childFragmentManager, fragmentContainer!!, tabLayout!!)
        tabLayoutPager.addFragment(CollectWeeklyFragment(), "周报")
        tabLayoutPager.addFragment(CollectLinkFragment(), "文章")
        tabLayoutPager.show()
    }
}