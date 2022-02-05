/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;

public class TestUtilsContentObject implements Serializable {

    private static final long serialVersionUID = 5379573854114004379L;

    private final int id;
    private final String name;
    private final float price;
    private final int count;
    private final long time;
    private final double total;
    private final TestUtilsContentObject next;

    public TestUtilsContentObject(int id, String name, float price, int count, long time, double total, TestUtilsContentObject next) {
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

    public TestUtilsContentObject getNext() {
        return next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestUtilsContentObject that = (TestUtilsContentObject) o;
        return id == that.id && Float.compare(that.price, price) == 0 && count == that.count && time == that.time && Double.compare(that.total, total) == 0 && name.equals(that.name)
                && ((next == null && that.next == null) || (next != null && next.equals(that.next)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count, time, total, next);
    }

    @NonNull
    @Override
    public String toString() {
        return "TestUtilsContentObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", time=" + time +
                ", total=" + total +
                ", next=" + next +
                '}';
    }
}
