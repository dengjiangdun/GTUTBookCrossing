package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/3/4.
 */

public class Comment extends BmobObject{
    private String time;
    private String content;
    private GDUTUser gdutUser;
    private Book book;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GDUTUser getGdutUser() {
        return gdutUser;
    }

    public void setGdutUser(GDUTUser gdutUser) {
        this.gdutUser = gdutUser;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
