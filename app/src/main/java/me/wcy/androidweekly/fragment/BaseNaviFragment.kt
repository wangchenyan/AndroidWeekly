package me.wcy.androidweekly.fragment

abstract class BaseNaviFragment : BaseLazyFragment() {
    abstract fun navigationMenuId(): Int

    open fun getMenuItemIds(): Array<Int> {
        return emptyArray()
    }
}