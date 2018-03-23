package me.wcy.androidweekly.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import me.wcy.androidweekly.R
import me.wcy.androidweekly.fragment.CollectionFragment
import me.wcy.androidweekly.fragment.WeeklyListFragment
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.widget.pager.FragmentAdapter
import me.wcy.androidweekly.widget.pager.ScrollableViewPager

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    private val drawerLayout: DrawerLayout? = null
    @Bind(R.id.navigation_view)
    private val navigationView: NavigationView? = null
    @Bind(R.id.toolbar)
    private val toolbar: Toolbar? = null
    @Bind(R.id.view_pager)
    private val viewPager: ScrollableViewPager? = null

    private var fragmentAdapter: FragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_menu)
        navigationView!!.setNavigationItemSelectedListener(this)
        title = "Android 开发技术周报"
        setupViewPager()
    }

    override fun shouldAddToolbar(): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            drawerLayout!!.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val fragmentList = mutableListOf<Fragment>()
        val titleList = mutableListOf<String>()
        fragmentList.add(WeeklyListFragment())
        fragmentList.add(CollectionFragment())
        titleList.add("Android 开发技术周报")
        titleList.add("收藏")
        fragmentAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
        fragmentAdapter!!.setTitleList(titleList)
        viewPager!!.setScrollable(false)
        viewPager.offscreenPageLimit = fragmentList.size
        viewPager.adapter = fragmentAdapter
        viewPager.addOnPageChangeListener(pageChangeListener)
        viewPager.setCurrentItem(0, false)
    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            title = fragmentAdapter!!.getPageTitle(position)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout!!.closeDrawers()
        when (item.itemId) {
            R.id.action_weekly -> {
                if (viewPager!!.currentItem != 0) {
                    viewPager.setCurrentItem(0, false)
                }
                return true
            }
            R.id.action_collection -> {
                if (viewPager!!.currentItem != 1) {
                    viewPager.setCurrentItem(1, false)
                }
                return true
            }
        }
        return false
    }
}
