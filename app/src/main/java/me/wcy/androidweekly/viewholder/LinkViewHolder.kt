package me.wcy.androidweekly.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.BrowserActivity
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.widget.radapter.RLayout
import me.wcy.androidweekly.widget.radapter.RViewHolder

/**
 * Created by hzwangchenyan on 2018/3/23.
 */
@RLayout(R.layout.view_holder_link)
class LinkViewHolder(item: View) : RViewHolder<Link>(item) {
    @Bind(R.id.tv_title)
    private val linkTitle: TextView? = null
    @Bind(R.id.tv_summary)
    private val linkSummary: TextView? = null

    override fun refresh() {
        linkTitle!!.text = data!!.title
        linkTitle.isSelected = ReadPreference.hasRead(data!!.url)
        linkSummary!!.text = data!!.summary
        linkSummary.visibility = if (TextUtils.isEmpty(data!!.summary)) View.GONE else View.VISIBLE
        item.setOnClickListener {
            BrowserActivity.start(context, data!!.url)
            ReadPreference.read(data!!.url)
            linkTitle.isSelected = true
        }
    }
}