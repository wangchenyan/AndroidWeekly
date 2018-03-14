package me.wcy.androidweekly.widget.radapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by wcy on 2017/11/26.
 */
class RAdapter<T>(private val dataList: MutableList<T>, private val delegate: RAdapterDelegate<T>) : RecyclerView.Adapter<RViewHolder<T>>() {
    private val viewHolderClassList: MutableList<Class<out RViewHolder<T>>>
    var tag: Any? = null

    companion object {
        private val TAG = "RAdapter"
    }

    init {
        viewHolderClassList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder<T> {
        try {
            val clazz = viewHolderClassList[viewType]
            val resId = getLayoutResId(clazz)
            val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
            val constructor = clazz.getConstructor(View::class.java)
            val viewHolder = constructor.newInstance(view) as RViewHolder<T>
            viewHolder.adapter = this
            return viewHolder
        } catch (e: Exception) {
            throw IllegalStateException("create view holder error", e)
        }
    }

    override fun onBindViewHolder(holder: RViewHolder<T>, position: Int) {
        try {
            holder.position = position
            holder.data = dataList[position]
            holder.refresh()
        } catch (e: Exception) {
            Log.e(TAG, "bind view holder error", e)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        val clazz = delegate.getViewHolderClass(position)
        if (!viewHolderClassList.contains(clazz)) {
            viewHolderClassList.add(clazz)
        }
        return viewHolderClassList.indexOf(clazz)
    }

    fun insertItem(t: T) {
        dataList.add(t)
        notifyItemInserted(dataList.size - 1)
    }

    fun insertItem(t: T, position: Int) {
        dataList.add(position, t)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getDataList(): List<T> {
        return dataList
    }

    private fun getLayoutResId(clazz: Class<*>): Int {
        if (clazz == RViewHolder::class.java) {
            return 0
        }
        // 找不到去父类找
        val layout = clazz.getAnnotation(RLayout::class.java) ?: return getLayoutResId(clazz.superclass)
        return layout.value
    }
}
