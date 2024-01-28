package com.example.chatuser.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    private final String formattedTime;

    public DateFormatUtil(String Format) {
        // 创建日期格式化对象
        SimpleDateFormat formatter = new SimpleDateFormat(Format);

        // 获取当前时间
        Date currentTime = new Date();

        // 按照指定格式格式化时间
        // 输出格式化后的时间
        this.formattedTime = formatter.format(currentTime);
    }

    public String get() {
        return formattedTime;
    }
}
