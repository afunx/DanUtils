/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
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
        TestUtilsContentObject next = new TestUtilsContentObject(nextId, nextName, nextPrice, nextCount, nextTime, nextTotal, null);
        int id = 10087;
        String name = "hello";
        float price = 987.654f;
        int count = 100;
        long time = Long.MAX_VALUE;
        double total = 98765.4;
        TestUtilsContentObject current = new TestUtilsContentObject(id, name, price, count, time, total, next);
        String serializableString = UtilsContentSerializable.writeObject(current);
        assertNotNull(serializableString);
        TestUtilsContentObject result = (TestUtilsContentObject) UtilsContentSerializable.readObject(serializableString);
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