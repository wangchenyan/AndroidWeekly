package me.wcy.androidweekly.api

import com.google.gson.JsonNull
import com.google.gson.JsonParser
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.wcy.androidweekly.converter.JobsConverter
import me.wcy.androidweekly.converter.WeeklyDetailConverter
import me.wcy.androidweekly.converter.WeeklyListConverter
import me.wcy.androidweekly.model.Jobs
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.model.WeeklyDetail
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
class Api private constructor() {
    private val api: IApi

    companion object {
        const val BASE_URL = "https://www.androidweekly.cn/"
        const val PAGE_SIZE = 6
        private val FORMAT = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        fun get() = SingletonHolder.instance
    }

    private object SingletonHolder {
        val instance: Api = Api()
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        api = retrofit.create(IApi::class.java)
    }

    fun getWeeklyList(page: Int): Single<List<Weekly>> {
        return api.getWeeklyList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    return@map WeeklyListConverter().convert(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSpecialWeeklyList(page: Int): Single<List<Weekly>> {
        return api.getSpecialWeeklyList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    return@map WeeklyListConverter().convert(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWeeklyDetail(url: String): Single<WeeklyDetail> {
        return api.getWeeklyDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    return@map WeeklyDetailConverter().convert(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun search(keyword: String): Single<MutableList<Weekly>> {
        var bodyString = "{\"requests\":[{\"indexName\":\"weeklyIndex\",\"params\":\"query=%s&page=0&facets=%s&tagFilters=\"}]}"
        val query = URLEncoder.encode(keyword, "UTF-8")
        val facets = URLEncoder.encode("[]", "UTF-8")
        bodyString = String.format(Locale.getDefault(), bodyString, query, facets)
        val body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), bodyString)
        return api.search(body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    val weeklies = mutableListOf<Weekly>()
                    val hits = JsonParser()
                            .parse(it)
                            .asJsonObject
                            .getAsJsonArray("results")
                            .get(0)
                            .asJsonObject
                            .getAsJsonArray("hits")
                    for (hit in hits) {
                        val weekly = Weekly()
                        weekly.title = hit.asJsonObject.get("title").asString
                        weekly.url = hit.asJsonObject.get("url").asString
                        val imageEle = hit.asJsonObject.get("image")
                        weekly.img = if (imageEle == null || imageEle == JsonNull.INSTANCE) null else imageEle.asString
                        val timestamp = hit.asJsonObject.get("timestamp").asLong
                        weekly.date = FORMAT.format(Date(timestamp))
                        weeklies.add(weekly)
                    }
                    return@map weeklies
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getJobs(): Single<Jobs> {
        return api.getJobs()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    return@map JobsConverter().convert(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}