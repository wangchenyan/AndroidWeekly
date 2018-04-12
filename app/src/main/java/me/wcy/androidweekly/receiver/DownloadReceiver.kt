package me.wcy.androidweekly.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import me.wcy.androidweekly.utils.ToastUtils
import java.io.File

/**
 * @author hzwangchenyan
 * @time 2018/4/12
 */
class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }

        val action = intent.action
        if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            try {
                onDownloadSuccess(context, intent)
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("自动安装失败")
            }
        } else if (action == DownloadManager.ACTION_NOTIFICATION_CLICKED) {
            // 点击通知取消下载
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)
            manager.remove(*ids)
            ToastUtils.show("已取消下载")
        }
    }

    private fun onDownloadSuccess(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = manager.query(query) ?: return

        if (cursor.moveToFirst()) {
            val fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            if (fileUri != null) {
                val file = File(Uri.parse(fileUri).path)
                if (file.exists()) {
                    install(context, file)
                }
            }
        }
        cursor.close()
    }

    private fun install(context: Context, file: File) {
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}