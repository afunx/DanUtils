/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import java.io.Serializable;

public class TestUtilsContentParcelable implements Serializable {

    private static final long serialVersionUID = 5379573854114004379L;

    private final int id;
    private final String name;
    private final float price;
    private final int count;
    private final long time;
    private final double total;
    private final TestUtilsContentParcelable next;

    public TestUtilsContentParcelable(int id, String name, float price, int count, long time, double total, TestUtilsContentParcelable next) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.time = time;
        this.total = total;
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public long getTime() {
        return time;
    }

    public double getTotal() {
        return total;
    }

    public TestUtilsContentParcelable getNext() {
        return next;
    }
}
