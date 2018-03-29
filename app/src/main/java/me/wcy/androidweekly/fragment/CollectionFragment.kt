package me.wcy.androidweekly.fragment

import kotlinx.android.synthetic.main.fragment_collection.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.widget.pager.TabLayoutPager

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
class CollectionFragment : BaseNaviFragment() {

    override fun layoutResId(): Int {
        return R.layout.fragment_collection
    }

    override fun navigationMenuId(): Int {
        return R.id.action_collection
    }

    override fun onLazyCreate() {
        val tabLayoutPager = TabLayoutPager(childFragmentManager, fragment_container, tab_layout)
        tabLayoutPager.addFragment(CollectWeeklyFragment(), "周报")
        tabLayoutPager.addFragment(CollectLinkFragment(), "文章")
        tabLayoutPager.show()
    }
}