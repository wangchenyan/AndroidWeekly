package me.wcy.androidweekly.viewholder

import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.link_item.view.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.BrowserActivity
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.widget.radapter.RLayout
import me.wcy.androidweekly.widget.radapter.RViewHolder

/**
 * Created by hzwangchenyan on 2018/3/23.
 */
@RLayout(R.layout.view_holder_link)
class LinkViewHolder(item: View) : RViewHolder<Link>(item) {

    override fun refresh() {
        item.tv_title.text = data!!.title
        item.tv_title.isSelected = ReadPreference.hasRead(data!!.url)
        item.tv_summary.text = data!!.summary
        item.tv_summary.visibility = if (TextUtils.isEmpty(data!!.summary)) View.GONE else View.VISIBLE
        item.setOnClickListener {
            BrowserActivity.start(context, data!!.url)
            ReadPreference.read(data!!.url)
            item.tv_title.isSelected = true
        }
    }
}