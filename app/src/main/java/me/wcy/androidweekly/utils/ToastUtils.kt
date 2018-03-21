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
    private var sContext: Context? = null
    private var sToast: Toast? = null

    fun init(context: Context) {
        sContext = context.applicationContext
    }

    fun show(resId: Int) {
        show(sContext!!.getString(resId))
    }

    @SuppressLint("ShowToast")
    fun show(text: String) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT)
        } else {
            sToast!!.setText(text)
        }
        sToast!!.show()
    }
}
