package me.wcy.androidweekly.widget.radapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

import me.wcy.androidweekly.utils.binding.ViewBinder

/**
 * Created by wcy on 2017/11/26.
 */
abstract class RViewHolder<T>(protected var item: View) : RecyclerView.ViewHolder(item) {
    protected val context: Context
    var adapter: RAdapter<T>? = null
    var data: T? = null
    var position: Int? = 0

    init {
        context = item.context
        ViewBinder.bind(this, item)
    }

    abstract fun refresh()
}
