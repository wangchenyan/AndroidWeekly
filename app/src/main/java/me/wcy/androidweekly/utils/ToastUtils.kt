package me.wcy.androidweekly.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * Toast工具类
 * Created by wcy on 2015/12/26.
 */
@SuppressLint("StaticFieldLeak")
object ToastUtils {
    private var context: Context? = null
    private var toast: Toast? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun show(resId: Int) {
        show(context!!.getString(resId))
    }

    @SuppressLint("ShowToast")
    fun show(text: String) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(text)
        }
        toast!!.show()
    }
}
