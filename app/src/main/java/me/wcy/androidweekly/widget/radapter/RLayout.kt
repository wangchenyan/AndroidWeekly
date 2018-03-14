package me.wcy.androidweekly.widget.radapter

import android.support.annotation.LayoutRes
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by hzwangchenyan on 2017/11/30.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class RLayout(@LayoutRes val value: Int)
