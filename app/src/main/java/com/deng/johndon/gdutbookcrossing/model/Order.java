package com.deng.johndon.gdutbookcrossing.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/3/4.
 */

public class Order extends BmobObject{
    private GDUTUser ower;
    private GDUTUser buyer;
    private Book book;

    public GDUTUser getBuyer() {
        return buyer;
    }

    public void setBuyer(GDUTUser buyer) {
        this.buyer = buyer;
    }

    public GDUTUser getOwer() {
        return ower;
    }

    public void setOwer(GDUTUser ower) {
        this.ower = ower;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
