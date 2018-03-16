package me.wcy.androidweekly.activity

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import me.wcy.androidweekly.R
import me.wcy.androidweekly.constants.Extras
import me.wcy.androidweekly.utils.binding.Bind

class BrowserActivity : BaseActivity() {
    @Bind(R.id.web_view)
    private val webView: WebView? = null
    @Bind(R.id.progress_bar)
    private val progressBar: ProgressBar? = null

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, BrowserActivity::class.java)
            intent.putExtra(Extras.TITLE, title)
            intent.putExtra(Extras.URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        window.setFormat(PixelFormat.TRANSLUCENT)

        val title = intent.getStringExtra(Extras.TITLE)
        val url = intent.getStringExtra(Extras.URL)

        setTitle(title)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        webView!!.settings.setSupportZoom(true)
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = webViewClient
        webView.webChromeClient = webChromeClient
        webView.loadUrl(url)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
