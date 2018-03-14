package me.wcy.androidweekly.widget.radapter

/**
 * Created by hzwangchenyan on 2018/1/17.
 */
class RSingleDelegate<T>(private val clazz: Class<out RViewHolder<T>>) : RAdapterDelegate<T> {

    override fun getViewHolderClass(position: Int): Class<out RViewHolder<T>> {
        return clazz
    }
}
