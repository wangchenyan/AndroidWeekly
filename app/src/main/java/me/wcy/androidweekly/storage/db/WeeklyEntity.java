package me.wcy.androidweekly.storage.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hzwangchenyan on 2018/3/19.
 */
@Entity(nameInDb = "weekly")
public class WeeklyEntity {
    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @NotNull
    @Unique
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

    @Property(nameInDb = "tag_list")
    private String tag_list;

    @Property(nameInDb = "author_name")
    private String author_name;

    @Property(nameInDb = "author_avatar")
    private String author_avatar;

    @Generated(hash = 402346306)
    public WeeklyEntity(Long id, @NotNull String url, String img,
            @NotNull String title, String date, String comment, String tag_list,
            String author_name, String author_avatar) {
        this.id = id;
        this.url = url;
        this.img = img;
        this.title = title;
        this.date = date;
        this.comment = comment;
        this.tag_list = tag_list;
        this.author_name = author_name;
        this.author_avatar = author_avatar;
    }

    @Generated(hash = 285986101)
    public WeeklyEntity() {
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

    public String getTag_list() {
        return this.tag_list;
    }

    public void setTag_list(String tag_list) {
        this.tag_list = tag_list;
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
}
