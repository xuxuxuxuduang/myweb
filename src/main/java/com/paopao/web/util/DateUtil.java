package com.paopao.web.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 泡泡
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获得前n天的日期
     * @param n
     * @return
     */
    public static Date getBeforeDays(int n){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,n * -1);
        return cal.getTime();
    }
}
