package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.hwangjr.rxbus.RxBus
import me.wcy.androidweekly.R

/**
 * Created by wcy on 2017/11/25.
 */
abstract class BaseLazyFragment : Fragment() {
    protected var handler: Handler? = null
    private var isLazyCreate: Boolean = false

    companion object {
        private const val TAG = "Fragment"
    }

    protected abstract fun layoutResId(): Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_lazy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handler = Handler()
        // 第一个 Fragment 在这里初始化
        if (userVisibleHint && !isLazyCreate) {
            userVisibleHint = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (!isLazyCreate) {
                lazyCreate()
            } else {
                onVisible()
            }
        } else if (isLazyCreate) {
            onInvisible()
        }
    }

    private fun lazyCreate() {
        if (view == null) {
            // not attach
            return
        }

        val rootView = view as LinearLayout?
        val view = LayoutInflater.from(context).inflate(layoutResId(), rootView, false)
        rootView!!.addView(view)
        Log.d(TAG, javaClass.simpleName + " onLazyCreate")
        onLazyCreate()
        isLazyCreate = true
        RxBus.get().register(this)
    }

    protected abstract fun onLazyCreate()

    override fun onDestroy() {
        if (isLazyCreate) {
            onLazyDestroy()
        }
        super.onDestroy()
    }

    protected fun onVisible() {}

    protected fun onInvisible() {}

    protected fun onLazyDestroy() {
        Log.d(TAG, javaClass.simpleName + " onLazyDestroy")
        RxBus.get().unregister(this)
    }
}
