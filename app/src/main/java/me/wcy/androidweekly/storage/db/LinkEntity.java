package me.wcy.androidweekly.storage.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by hzwangchenyan on 2018/3/22.
 */
@Entity(nameInDb = "favorite_link")
public class LinkEntity {
    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "url")
    private String url;

    @NotNull
    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "summary")
    private String summary;

    @Property(nameInDb = "time")
    private Long time;

    @Generated(hash = 1585243722)
    public LinkEntity() {
    }

    @Generated(hash = 1107041173)
    public LinkEntity(Long id, @NotNull String url, @NotNull String title,
                      String summary, Long time) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.summary = summary;
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
