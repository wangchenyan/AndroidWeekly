package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.View
import me.wcy.androidweekly.utils.binding.ViewBinder

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
abstract class BaseFragment : Fragment() {
    protected var handler: Handler? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler = Handler()
        if (view != null) {
            ViewBinder.bind(this, view)
        }
    }
}