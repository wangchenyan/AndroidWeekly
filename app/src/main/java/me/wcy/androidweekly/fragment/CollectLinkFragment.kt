package me.wcy.androidweekly.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import me.wcy.androidweekly.R
import me.wcy.androidweekly.constants.RxBusTags
import me.wcy.androidweekly.model.Link
import me.wcy.androidweekly.storage.db.DBManager
import me.wcy.androidweekly.storage.db.greendao.LinkDao
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.viewholder.LinkViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

/**
 * Created by hzwangchenyan on 2018/3/23.
 */
class CollectLinkFragment : BaseLazyFragment() {
    @Bind(R.id.rv_collect_link)
    private val rvCollectLink: RecyclerView? = null

    private val linkList = mutableListOf<Link>()
    private val adapter = RAdapter(linkList, RSingleDelegate(LinkViewHolder::class.java))

    override fun layoutResId(): Int {
        return R.layout.fragment_collect_link
    }

    override fun onLazyCreate() {
        rvCollectLink!!.layoutManager = LinearLayoutManager(context)
        rvCollectLink.adapter = adapter
        showList()
    }

    private fun showList() {
        val list = DBManager.get().getLinkEntityDao()!!.queryBuilder().orderDesc(LinkDao.Properties.Time).list()
        linkList.clear()
        linkList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    @Suppress("unused")
    @Subscribe(tags = [(Tag(RxBusTags.LINK_COLLECTION))])
    fun onCollect(url: String) {
        showList()
    }
}