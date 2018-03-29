package me.wcy.androidweekly.viewholder

import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_holder_weekly.view.*
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.WeeklyDetailActivity
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.storage.sp.ReadPreference
import me.wcy.androidweekly.widget.radapter.RLayout
import me.wcy.androidweekly.widget.radapter.RViewHolder

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
@RLayout(R.layout.view_holder_weekly)
class WeeklyViewHolder(item: View) : RViewHolder<Weekly>(item) {

    init {
        item.setOnClickListener {
            WeeklyDetailActivity.start(context, data!!)
            ReadPreference.read(data!!.url)
            refresh()
        }
    }

    override fun refresh() {
        Glide.with(context)
                .load(data!!.img)
                .placeholder(R.drawable.image_placeholder)
                .into(item.iv_image)
        item.tv_title.text = data!!.title
        item.tv_title.isSelected = ReadPreference.hasRead(data!!.url)
        item.tv_date.text = if (TextUtils.isEmpty(data!!.date)) null else data!!.date
        item.tv_comment.text = if (TextUtils.isEmpty(data!!.comment)) null else data!!.comment
        item.tv_tags.text = if (TextUtils.isEmpty(data!!.tags)) null else data!!.tags
    }
}