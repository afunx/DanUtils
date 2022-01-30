/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import static com.dan.me.utils.content.UtilsContentConstants.BOOLEAN_CODE;
import static com.dan.me.utils.content.UtilsContentConstants.INTEGER_CODE;
import static com.dan.me.utils.content.UtilsContentConstants.LONG_CODE;
import static com.dan.me.utils.content.UtilsContentConstants.STRING_CODE;

public enum UtilsContentTypeEnum {

    BOOLEAN(BOOLEAN_CODE),
    INTEGER(INTEGER_CODE),
    LONG(LONG_CODE),
    STRING(STRING_CODE);

    private final int code;

    UtilsContentTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
