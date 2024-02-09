package com.tsdb.common.utils;

import com.tsdb.common.config.TSDBConstant;

public class StringUtil {

    public static int calcLength(String str){
        return str.getBytes(TSDBConstant.STRING_CHARSET).length;
    }
}
