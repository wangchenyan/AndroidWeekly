package me.wcy.androidweekly.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
interface IApi {

    @GET("page/{page}/")
    fun getWeeklyList(@Path("page") page: Int): Single<String>

    @GET
    fun getWeeklyDetail(@Url url: String): Single<String>
}