package me.wcy.androidweekly.api

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.wcy.androidweekly.converter.WeeklyDetailConverter
import me.wcy.androidweekly.converter.WeeklyListConverter
import me.wcy.androidweekly.model.Weekly
import me.wcy.androidweekly.model.WeeklyDetail
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
class Api private constructor() {
    private val api: IApi

    companion object {
        const val BASE_URL: String = "https://www.androidweekly.cn/"
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

    fun getWeeklyDetail(url: String): Single<WeeklyDetail> {
        return api.getWeeklyDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map {
                    return@map WeeklyDetailConverter().convert(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}