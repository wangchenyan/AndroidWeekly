package me.wcy.androidweekly.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import me.wcy.androidweekly.R
import me.wcy.androidweekly.fragment.CollectionFragment
import me.wcy.androidweekly.fragment.WeeklyListFragment
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.widget.FragmentAdapter
import me.wcy.androidweekly.widget.ScrollableViewPager

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    private val drawerLayout: DrawerLayout? = null
    @Bind(R.id.navigation_view)
    private val navigationView: NavigationView? = null
    @Bind(R.id.toolbar)
    private val toolbar: Toolbar? = null
    @Bind(R.id.view_pager)
    private val viewPager: ScrollableViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        navigationView!!.setNavigationItemSelectedListener(this)
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
        fragmentList.add(WeeklyListFragment())
        fragmentList.add(CollectionFragment())
        val fragmentAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
        viewPager!!.setScrollable(false)
        viewPager.offscreenPageLimit = fragmentList.size
        viewPager.adapter = fragmentAdapter
        viewPager.setCurrentItem(0, false)
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
