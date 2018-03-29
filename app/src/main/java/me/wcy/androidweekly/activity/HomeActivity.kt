package me.wcy.androidweekly.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import me.wcy.androidweekly.R
import me.wcy.androidweekly.fragment.BaseNaviFragment
import me.wcy.androidweekly.fragment.CollectionFragment
import me.wcy.androidweekly.fragment.JobsFragment
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

    private val fragmentList = mutableListOf<BaseNaviFragment>()
    private val navigationIndex = mutableListOf<Int>()
    private var fragmentAdapter: FragmentAdapter? = null
    private var menu: Menu? = null

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }
        for (f in fragmentList) {
            f.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager() {
        val titleList = mutableListOf<String>()
        fragmentList.add(WeeklyListFragment.newInstance(WeeklyListFragment.TYPE_WEEKLY))
        fragmentList.add(CollectionFragment())
        fragmentList.add(WeeklyListFragment.newInstance(WeeklyListFragment.TYPE_SPECIAL))
        fragmentList.add(JobsFragment())
        titleList.add("Android 开发技术周报")
        titleList.add("收藏")
        titleList.add("特刊")
        titleList.add("酷工作")
        fragmentAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
        fragmentAdapter!!.setTitleList(titleList)
        viewPager!!.setScrollable(false)
        viewPager.offscreenPageLimit = fragmentList.size
        viewPager.adapter = fragmentAdapter
        viewPager.addOnPageChangeListener(pageChangeListener)
        viewPager.setCurrentItem(0, false)

        for (f in fragmentList) {
            navigationIndex.add(f.navigationMenuId())
        }
    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            title = fragmentAdapter!!.getPageTitle(position)
            for (i in 0 until menu!!.size()) {
                menu!!.getItem(i).isVisible = false
            }
            val fragment = fragmentList[position]
            for (id in fragment.getMenuItemIds()) {
                menu!!.findItem(id).isVisible = true
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout!!.closeDrawers()
        val index = navigationIndex.indexOf(item.itemId)
        if (index >= 0) {
            viewPager!!.setCurrentItem(index, false)
            return true
        }
        when (item.itemId) {
            R.id.action_contribute -> {
                val url = "https://gdgdocs.org/r/A8lDj5"
                BrowserActivity.start(this, url)
                return true
            }
            R.id.action_setting -> {
                SettingActivity.start(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
