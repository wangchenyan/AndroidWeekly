package me.wcy.androidweekly.storage.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
@Entity(nameInDb = "favorite_weekly")
public class WeeklyEntity {
    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "url")
    private String url;

    @Property(nameInDb = "img")
    private String img;

    @NotNull
    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "date")
    private String date;

    @Property(nameInDb = "comment")
    private String comment;

    @Property(nameInDb = "tags")
    private String tags;

    @Property(nameInDb = "author_name")
    private String author_name;

    @Property(nameInDb = "author_avatar")
    private String author_avatar;

    @Property(nameInDb = "time")
    private Long time;

    @Generated(hash = 285986101)
    public WeeklyEntity() {
    }

    @Generated(hash = 1144720190)
    public WeeklyEntity(Long id, @NotNull String url, String img, @NotNull String title,
                        String date, String comment, String tags, String author_name,
                        String author_avatar, Long time) {
        this.id = id;
        this.url = url;
        this.img = img;
        this.title = title;
        this.date = date;
        this.comment = comment;
        this.tags = tags;
        this.author_name = author_name;
        this.author_avatar = author_avatar;
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

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAuthor_name() {
        return this.author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_avatar() {
        return this.author_avatar;
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
