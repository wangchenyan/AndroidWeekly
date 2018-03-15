package me.wcy.androidweekly.api

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import me.wcy.androidweekly.activity.BaseActivity
import java.lang.ref.WeakReference

/**
 * 避免内存泄露的Observer
 * Created by hzwangchenyan on 2017/11/30.
 */
abstract class SafeObserver<T> : SingleObserver<T> {
    private var activityRef: WeakReference<BaseActivity>? = null
    private var fragmentRef: WeakReference<Fragment>? = null

    constructor(context: Context) {
        if (context is BaseActivity) {
            activityRef = WeakReference(context)
        }
    }

    constructor(activity: BaseActivity?) {
        if (activity != null) {
            activityRef = WeakReference(activity)
        }
    }

    constructor(fragment: Fragment?) {
        if (fragment != null) {
            fragmentRef = WeakReference(fragment)
        }
    }

    override fun onSubscribe(d: Disposable) {}

    override fun onSuccess(result: T) {
        if (dispatch()) {
            onResult(result, null)
        }
    }

    override fun onError(e: Throwable) {
        Log.e(getTag(), "request error", e)
        if (dispatch()) {
            onResult(null, e)
        }
    }

    abstract fun onResult(t: T?, e: Throwable?)

    private fun dispatch(): Boolean {
        if (activityRef != null) {
            val activity = activityRef!!.get()
            return activity != null && !activity.isDestroyedCompat()
        } else if (fragmentRef != null) {
            val fragment = fragmentRef!!.get()
            return fragment != null && fragment.isAdded
        }
        return true
    }

    private fun getTag(): String {
        if (activityRef != null && activityRef!!.get() != null) {
            return activityRef!!.get()!!.javaClass.simpleName
        } else if (fragmentRef != null && fragmentRef!!.get() != null) {
            return fragmentRef!!.get()!!.javaClass.simpleName
        }
        return "API"
    }
}
