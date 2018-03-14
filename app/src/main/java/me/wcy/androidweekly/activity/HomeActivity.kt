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
import me.wcy.androidweekly.fragment.WeeklyFragment
import me.wcy.androidweekly.utils.binding.Bind

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    private val drawerLayout: DrawerLayout? = null
    @Bind(R.id.navigation_view)
    private val navigationView: NavigationView? = null
    @Bind(R.id.toolbar)
    private val toolbar: Toolbar? = null

    private var weeklyFragment: WeeklyFragment? = null
    private var collectionFragment: CollectionFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        weeklyFragment = WeeklyFragment()
        replaceFragment(weeklyFragment!!)
        navigationView!!.setNavigationItemSelectedListener(this)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout!!.closeDrawers()
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.action_weekly -> {
                if (weeklyFragment == null) {
                    weeklyFragment = WeeklyFragment()
                }
                fragment = weeklyFragment as WeeklyFragment
            }
            R.id.action_collection -> {
                if (collectionFragment == null) {
                    collectionFragment = CollectionFragment()
                }
                fragment = collectionFragment as CollectionFragment
            }
        }
        if (fragment != null) {
            replaceFragment(fragment)
            return true
        }
        return false
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
    }
}
