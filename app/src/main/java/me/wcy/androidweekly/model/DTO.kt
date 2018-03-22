package me.wcy.androidweekly.model

import me.wcy.androidweekly.storage.db.LinkEntity
import me.wcy.androidweekly.storage.db.WeeklyEntity

/**
 * Created by hzwangchenyan on 2018/3/22.
 */
object DTO {

    fun toWeeklyEntity(weekly: Weekly): WeeklyEntity {
        val entity = WeeklyEntity()
        entity.url = weekly.url
        entity.img = weekly.img
        entity.title = weekly.title
        entity.date = weekly.date
        entity.comment = weekly.comment
        entity.tags = weekly.tags
        entity.author_name = weekly.author_name
        entity.author_avatar = weekly.author_avatar
        entity.time = System.currentTimeMillis()
        return entity
    }

    fun toLinkEntity(link: Link): LinkEntity {
        val entity = LinkEntity()
        entity.url = link.url
        entity.title = link.title
        entity.summary = link.summary
        entity.time = System.currentTimeMillis()
        return entity
    }
}