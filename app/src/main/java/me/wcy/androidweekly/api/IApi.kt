package me.wcy.androidweekly.api

import io.reactivex.Single
import me.wcy.androidweekly.model.Version
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
interface IApi {

    @GET("page/{page}/")
    fun getWeeklyList(@Path("page") page: Int): Single<String>

    @GET("tag/androiddevspecialweekly/page/{page}/")
    fun getSpecialWeeklyList(@Path("page") page: Int): Single<String>

    @GET
    fun getWeeklyDetail(@Url url: String): Single<String>

    @POST("https://1hjizn5dia-dsn.algolia.net/1/indexes/*/queries?x-algolia-agent=Algolia%20for%20vanilla%20JavaScript%20(lite)%203.26.0%3BJS%20Helper%202.23.2&x-algolia-application-id=1HJIZN5DIA&x-algolia-api-key=936b8acf3a8d1dd7ea0cad3d011bd307")
    fun search(@Body body: RequestBody): Single<String>

    @GET("cool-jobs/")
    fun getJobs(): Single<String>

    @GET("https://raw.githubusercontent.com/wangchenyan/AndroidWeekly/master/version.json")
    fun getVersion(): Single<Version>
}