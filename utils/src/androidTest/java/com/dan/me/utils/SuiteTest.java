/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils;

import com.dan.me.utils.bytes.BytesUtilsTest;
import com.dan.me.utils.content.UtilsContentCacheHelperTest;
import com.dan.me.utils.content.UtilsContentEscapeTest;
import com.dan.me.utils.content.UtilsContentHelperTest;
import com.dan.me.utils.content.UtilsContentSerializableTest;
import com.dan.me.utils.io.AssetsUtils;
import com.dan.me.utils.io.FileUtilsTest;
import com.dan.me.utils.io.ZipUtilsTest;
import com.dan.me.utils.log.LogUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BytesUtilsTest.class, UtilsContentCacheHelperTest.class,
        UtilsContentEscapeTest.class, UtilsContentHelperTest.class,
        UtilsContentSerializableTest.class, FileUtilsTest.class,
        LogUtilsTest.class, AssetsUtils.class, ZipUtilsTest.class})
public class SuiteTest {
}
