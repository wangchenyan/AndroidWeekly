package me.wcy.androidweekly.widget.radapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by wcy on 2017/11/26.
 */
abstract class RViewHolder<T>(protected var item: View) : RecyclerView.ViewHolder(item) {
    protected val context = item.context!!
    var adapter: RAdapter<T>? = null
    var data: T? = null
    var position: Int? = 0

    abstract fun refresh()
}
