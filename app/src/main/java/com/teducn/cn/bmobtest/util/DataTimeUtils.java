package com.teducn.cn.bmobtest.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tarena on 2017/8/3.
 */

public class DataTimeUtils {
    /**
     * 格式化时间的工具
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    /**
     * 被格式化的时间对象
     */
    private static Date date = new Date();
    /**
     * 默认的模式字符串
     */
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取应用于播放器上的时间字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为mm:ss 格式的字符串
     */
    public static String getPlayerTime(long millis) {
        if (millis >= 1 * 60 * 60 * 1000) {
            return getPlayerLongTime(millis);
        } else {
            return getPlayerShortTime(millis);
        }
    }

    /**
     * 获取应用于播放器上的时间的字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为 mm:ss 格式的字符串
     */
    public static String getPlayerShortTime(long millis) {
        return getFormattedTime(millis, "mm:ss");
    }

    /**
     * 获取应用于播放器上的时间的字符串
     *
     * @param millis 需要被格式化的时间，单位：毫秒
     * @return 格式化为 HH:mm:ss 格式的字符串
     */
    public static String getPlayerLongTime(long millis) {
        return getFormattedTime(millis - 8 * 60 * 60 * 1000, "HH:mm:ss");
    }

    /**
     * 将long类型的时间戳格式化为“yyyy-MM-dd HH:mm:ss”模式的String
     *
     * @param millis 需要被格式化的时间的时间戳
     * @return 格式化为“yyyy-MM-dd HH:mm:ss”模式的String
     */
    public static String getFormattedTime(long millis) {
        return getFormattedTime(millis, PATTERN_DEFAULT);
    }

    /**
     * 将long类型的时间戳格式化为指定模式的String
     *
     * @param millis  需要被格式化的时间的时间戳
     * @param pattern 格式化时使用的模式字符串，例如：{@link #PATTERN_DEFAULT}
     * @return 格式化为pattern模式的String
     */
    public static String getFormattedTime(long millis, String pattern) {
        sdf.applyPattern(pattern);
        date.setTime(millis);
        return sdf.format(date);
    }
}
