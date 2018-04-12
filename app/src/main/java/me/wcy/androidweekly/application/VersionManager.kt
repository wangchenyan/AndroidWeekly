package me.wcy.androidweekly.application

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.webkit.MimeTypeMap
import me.wcy.androidweekly.BuildConfig
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.model.Version
import me.wcy.androidweekly.utils.ToastUtils
import java.util.*

/**
 * @author hzwangchenyan
 * @time 2018/4/12
 */
class VersionManager {

    companion object {
        fun get() = SingletonHolder.instance
    }

    private object SingletonHolder {
        val instance = VersionManager()
    }

    fun checkVersion(context: Context, silent: Boolean) {
        if (!silent) {
            ToastUtils.show("正在检查更新")
        }
        Api.get().getVersion()
                .subscribe(object : SafeObserver<Version>(context) {
                    override fun onResult(t: Version?, e: Throwable?) {
                        if (e == null && t != null) {
                            compareVersion(context, t)
                        } else if (!silent) {
                            ToastUtils.show("检查更新失败")
                        }
                    }
                })
    }

    private fun compareVersion(context: Context, version: Version) {
        if (version.code!!.toInt() > BuildConfig.VERSION_CODE) {
            val message = "${version.name}(${b2mb(version.size!!)}MB)\n\n${version.desc}"
            AlertDialog.Builder(context)
                    .setTitle("发现新版本")
                    .setMessage(message)
                    .setPositiveButton("立即更新", { _, _ -> download(context, version) })
                    .setNegativeButton("稍后提醒", null)
                    .setCancelable(false)
                    .show()
        }
    }

    private fun download(context: Context, version: Version) {
        val fileName = getFileNameFromUrl(version.url!!)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(version.url)
        val request = DownloadManager.Request(uri)
        request.setTitle(context.getString(R.string.app_name))
        request.setDescription("正在更新…")
        request.setDestinationInExternalFilesDir(context, "download", fileName)
        request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(version.url))
        request.allowScanningByMediaScanner()
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setAllowedOverRoaming(false) // 不允许漫游
        downloadManager.enqueue(request)
        ToastUtils.show("正在后台下载")
    }

    private fun getFileNameFromUrl(url: String): String {
        var path = url
        if (!TextUtils.isEmpty(path)) {
            val fragment = path.lastIndexOf('#')
            if (fragment > 0) {
                path = path.substring(0, fragment)
            }

            val query = path.lastIndexOf('?')
            if (query > 0) {
                path = path.substring(0, query)
            }

            val filenamePos = path.lastIndexOf('/')
            return if (0 <= filenamePos) path.substring(filenamePos + 1) else path
        }

        return ""
    }

    private fun b2mb(b: Long): Float {
        val mb = String.format(Locale.getDefault(), "%.2f", b.toFloat() / 1024f / 1024f)
        return mb.toFloat()
    }
}