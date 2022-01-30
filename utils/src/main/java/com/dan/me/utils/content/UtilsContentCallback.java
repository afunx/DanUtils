/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface UtilsContentCallback<T> {
    /**
     * 数据发生变化回调。第一次注册时，会自动回调最新数据。
     * 并且，第一次回调时oldValue一定是null。
     *
     * @param oldValue  旧数据
     * @param newValue  新数据
     */
    void onValueChanged(@Nullable T oldValue, @NonNull T newValue);

    /**
     * 数据删除回调
     */
    void onValueDeleted();
}
