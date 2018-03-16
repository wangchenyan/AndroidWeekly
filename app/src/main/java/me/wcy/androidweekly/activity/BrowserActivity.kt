package me.wcy.androidweekly.activity

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
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
        webView!!.webChromeClient = webChromeClient
        webView.loadUrl(url)
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(p0: WebView?, p1: Int) {
            super.onProgressChanged(p0, p1)
            progressBar!!.progress = p1
            progressBar.visibility = if (p1 == 100) View.GONE else View.VISIBLE
        }
    }
}
