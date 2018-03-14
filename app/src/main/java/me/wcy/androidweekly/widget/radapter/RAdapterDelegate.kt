package me.wcy.androidweekly.widget.radapter

/**
 * Created by wcy on 2017/11/26.
 */
interface RAdapterDelegate<T> {
    fun getViewHolderClass(position: Int): Class<out RViewHolder<T>>
}
