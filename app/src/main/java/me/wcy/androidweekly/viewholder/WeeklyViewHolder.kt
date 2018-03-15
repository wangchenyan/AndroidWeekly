package me.wcy.androidweekly.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import me.wcy.androidweekly.R
import me.wcy.androidweekly.activity.WeeklyDetailActivity
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.widget.radapter.RLayout
import me.wcy.androidweekly.widget.radapter.RViewHolder

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
@RLayout(R.layout.view_holder_weekly)
class WeeklyViewHolder(item: View) : RViewHolder<Weekly>(item) {
    @Bind(R.id.iv_avatar)
    private val ivAvatar: ImageView? = null
    @Bind(R.id.tv_author)
    private val tvAuthor: TextView? = null
    @Bind(R.id.iv_image)
    private val image: ImageView? = null
    @Bind(R.id.tv_title)
    private val tvTitle: TextView? = null
    @Bind(R.id.tv_date)
    private val tvDate: TextView? = null
    @Bind(R.id.tv_comment)
    private val tvComment: TextView? = null
    @Bind(R.id.tv_tags)
    private val tvTags: TextView? = null

    init {
        item.setOnClickListener {
            WeeklyDetailActivity.start(context, data!!.title!!, data!!.url!!)
        }
    }

    override fun refresh() {
        Glide.with(context)
                .load(data!!.author_avatar)
                .asBitmap()
                .into(ivAvatar)
        tvAuthor!!.text = data!!.author_name
        Glide.with(context)
                .load(data!!.img)
                .into(image)
        tvTitle!!.text = data!!.title
        tvDate!!.text = data!!.date
        tvComment!!.text = data!!.comment
        var tags = ""
        data!!.tagList!!.forEach {
            tags = tags.plus(it)
        }
        tvTags!!.text = tags
    }
}