package me.wcy.androidweekly.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by hzwangchenyan on 2018/3/14.
 */
interface IApi {

    @GET("page/{page}/")
    fun getWeeklyList(@Path("page") page: Int): Single<String>
}