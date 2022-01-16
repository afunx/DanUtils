/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils;

import com.dan.me.utils.bytes.BytesUtilsTest;
import com.dan.me.utils.io.FileUtilsTest;
import com.dan.me.utils.log.LogUtilsTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BytesUtilsTest.class, FileUtilsTest.class, LogUtilsTest.class})
public class SuiteTest {
}
