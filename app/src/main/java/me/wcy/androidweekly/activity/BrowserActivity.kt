package me.wcy.androidweekly.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.hwangjr.rxbus.RxBus
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import me.wcy.androidweekly.R
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.constants.RxBusTags
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.LinkDao
import me.wcy.androidweekly.utils.ToastUtils
import me.wcy.androidweekly.utils.binding.Bind

class BrowserActivity : BaseActivity() {
    @Bind(R.id.web_view)
    private val webView: WebView? = null
    @Bind(R.id.progress_bar)
    private val progressBar: ProgressBar? = null

    private var link: Link? = null

    companion object {
        fun start(context: Context, link: Link) {
            val intent = Intent(context, BrowserActivity::class.java)
            intent.putExtra(Extras.LINK, link)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        window.setFormat(PixelFormat.TRANSLUCENT)

        link = intent.getSerializableExtra(Extras.LINK) as Link

        title = link!!.title
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_close)

        val webSetting = webView!!.settings
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        webSetting.loadWithOverviewMode = true
        webSetting.setAppCacheEnabled(true)
        webSetting.databaseEnabled = true
        webSetting.domStorageEnabled = true
        webSetting.javaScriptEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.setAppCachePath(getDir("appcache", 0).path)
        webSetting.databasePath = getDir("databases", 0).path
        webSetting.setGeolocationDatabasePath(getDir("geolocation", 0).path)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)

        webView.webViewClient = webViewClient
        webView.webChromeClient = webChromeClient
        webView.loadUrl(link!!.url)
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
            p0!!.loadUrl(p1)
            return true
        }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(p0: WebView?, p1: Int) {
            super.onProgressChanged(p0, p1)
            progressBar!!.progress = p1
            progressBar.visibility = if (p1 == 100) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        val collectItem = menu!!.findItem(R.id.action_collect)
        val collection = DBManager.get().getLinkEntityDao()!!.queryBuilder().where(LinkDao.Properties.Url.eq(link!!.url)).unique()
        setCollectMenuItem(collectItem, collection != null)
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return super.onMenuOpened(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_collect -> {
                val collected: Boolean
                if (!DBManager.get().hasCollect(link!!)) {
                    collected = true
                    DBManager.get().getLinkEntityDao()!!.insert(link)
                } else {
                    collected = false
                    val entity = DBManager.get().getLinkEntityDao()!!.queryBuilder().where(LinkDao.Properties.Url.eq(link!!.url)).build().unique()
                    if (entity != null) {
                        DBManager.get().getLinkEntityDao()!!.delete(entity)
                    }
                }
                RxBus.get().post(RxBusTags.LINK_COLLECTION, link)
                setCollectMenuItem(item, collected)
                ToastUtils.show(if (collected) "已收藏" else "已取消收藏")
                return true
            }
            R.id.action_open_in_browser -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link!!.url))
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.show("打开失败")
                }
                return true
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, link!!.title.plus("\n").plus(link!!.url))
                startActivity(Intent.createChooser(intent, "分享"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCollectMenuItem(item: MenuItem, collected: Boolean) {
        item.title = if (collected) "已收藏" else "收藏"
        item.setIcon(if (collected) R.drawable.ic_menu_star_selected else R.drawable.ic_menu_star)
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
