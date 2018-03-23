package me.wcy.androidweekly.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.hwangjr.rxbus.RxBus
import me.wcy.androidweekly.R
import me.wcy.androidweekly.utils.binding.ViewBinder

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
        RxBus.get().register(this)
    }

    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, null)
        setContentView(view)
    }

    override fun setContentView(view: View?) {
        super.setContentView(realContent(view!!))
        ViewBinder.bind(this)
    }

    private fun realContent(view: View): View {
        if (shouldAddToolbar()) {
            val content = LinearLayout(this)
            content.orientation = LinearLayout.VERTICAL
            val appbar = LayoutInflater.from(this).inflate(R.layout.include_app_bar, content, false) as AppBarLayout
            val toolbar = appbar.findViewById<Toolbar>(R.id.toolbar)
            content.addView(appbar)
            content.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            return content
        }
        return view
    }

    protected open fun shouldAddToolbar(): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun isDestroyedCompat(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            super.isDestroyed()
        } else {
            supportFragmentManager.isDestroyed
        }
    }

    override fun onDestroy() {
        RxBus.get().unregister(this)
        super.onDestroy()
    }
}