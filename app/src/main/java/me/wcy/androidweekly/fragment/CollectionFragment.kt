package me.wcy.androidweekly.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.wcy.androidweekly.R

/**
 * Created by hzwangchenyan on 2018/3/13.
 */
class CollectionFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_collection, container, false)
    }
}