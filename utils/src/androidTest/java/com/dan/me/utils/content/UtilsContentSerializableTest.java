/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsContentSerializableTest {

    @Test
    public void readSerializable() {
        // 在writeSerializable()测试
    }

    @Test
    public void writeSerializable() {
        //int id, String name, float price, int count, long time, double total, TestUtilsContentParcelable next
        int nextId = 10086;
        String nextName = "dan";
        float nextPrice = 123.456f;
        int nextCount = 10;
        long nextTime = Long.MIN_VALUE;
        double nextTotal = 1234.56;
        TestUtilsContentParcelable next = new TestUtilsContentParcelable(nextId, nextName, nextPrice, nextCount, nextTime, nextTotal, null);
        int id = 10087;
        String name = "hello";
        float price = 987.654f;
        int count = 100;
        long time = Long.MAX_VALUE;
        double total = 98765.4;
        TestUtilsContentParcelable current = new TestUtilsContentParcelable(id, name, price, count, time, total, next);
        String serializableString = UtilsContentSerializable.writeObject(current);
        assertNotNull(serializableString);
        TestUtilsContentParcelable result = (TestUtilsContentParcelable) UtilsContentSerializable.readObject(serializableString);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(price, result.getPrice(), 0.0);
        assertEquals(count, result.getCount());
        assertEquals(time, result.getTime());
        assertEquals(total, result.getTotal(), 0.0);
        assertNotNull(result.getNext());
        assertEquals(nextId, result.getNext().getId());
        assertEquals(nextName, result.getNext().getName());
        assertEquals(nextPrice, result.getNext().getPrice(), 0.0);
        assertEquals(nextCount, result.getNext().getCount());
        assertEquals(nextTime, result.getNext().getTime());
        assertEquals(nextTotal, result.getNext().getTotal(), 0.0);
    }
}