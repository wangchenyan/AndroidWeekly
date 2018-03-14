package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import me.wcy.androidweekly.R
import me.wcy.androidweekly.utils.binding.ViewBinder

/**
 * Created by wcy on 2017/11/25.
 */
abstract class BaseLazyFragment : Fragment() {
    protected var handler: Handler? = null
    private var isLazyCreate: Boolean = false

    companion object {
        private val TAG = "Fragment"
    }

    protected abstract val layoutResId: Int

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
        val view = LayoutInflater.from(context).inflate(layoutResId, rootView, false)
        rootView!!.addView(view)
        ViewBinder.bind(this, rootView)
        Log.d(TAG, javaClass.simpleName + " onLazyCreate")
        onLazyCreate()
        isLazyCreate = true
        //RxBus.get().register(this);
    }

    protected abstract fun onLazyCreate()

    override fun onDestroy() {
        super.onDestroy()
        if (isLazyCreate) {
            onLazyDestroy()
        }
    }

    protected fun onVisible() {}

    protected fun onInvisible() {}

    protected fun onLazyDestroy() {
        Log.d(TAG, javaClass.simpleName + " onLazyDestroy")
        //RxBus.get().unregister(this);
    }
}
