package me.wcy.androidweekly.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.wcy.androidweekly.R
import me.wcy.androidweekly.api.Api
import me.wcy.androidweekly.api.SafeObserver
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.utils.ListUtils
import me.wcy.androidweekly.utils.binding.Bind
import me.wcy.androidweekly.viewholder.WeeklyViewHolder
import me.wcy.androidweekly.widget.radapter.RAdapter
import me.wcy.androidweekly.widget.radapter.RSingleDelegate

class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener {
    @Bind(R.id.tv_label)
    private val tvLabel: TextView? = null
    @Bind(R.id.rv_weekly)
    private val rvWeekly: RecyclerView? = null

    private val weeklyList = mutableListOf<Weekly>()
    private val adapter = RAdapter(weeklyList, RSingleDelegate(WeeklyViewHolder::class.java))

    companion object {
        fun start(content: Context) {
            val intent = Intent(content, SearchActivity::class.java)
            content.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvWeekly!!.layoutManager = LinearLayoutManager(this)
        rvWeekly.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_input, menu)
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = "终于能搜索了，快试试"
        searchView.setOnQueryTextListener(this)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_menu_search)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val keyword = query!!.trim()
        if (!TextUtils.isEmpty(keyword)) {
            search(keyword)
            return true
        }
        return false
    }

    private fun search(keyword: String) {
        tvLabel!!.visibility = View.VISIBLE
        rvWeekly!!.visibility = View.GONE
        tvLabel.text = "正在搜索…"
        rvWeekly.tag = keyword
        Api.get().search(keyword)
                .subscribe(object : SafeObserver<MutableList<Weekly>>(this) {
                    override fun onResult(t: MutableList<Weekly>?, e: Throwable?) {
                        if (keyword != rvWeekly.tag) {
                            return
                        }
                        if (e == null) {
                            if (!ListUtils.isEmpty(t)) {
                                tvLabel.visibility = View.GONE
                                rvWeekly.visibility = View.VISIBLE
                                weeklyList.clear()
                                weeklyList.addAll(t!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                tvLabel.text = "搜索结果为空"
                            }
                        } else {
                            tvLabel.text = "搜索失败，请稍后再试"
                        }
                    }
                })
    }
}
