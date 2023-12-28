package com.example.chatuser.util;

import java.time.Duration;

public class TimeConversionUtil {

    public static String convertSecondsToTime(long seconds) {
        Duration duration = Duration.ofSeconds(seconds);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long remainingSeconds = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天 ");
        }
        if (hours > 0) {
            sb.append(hours).append("小时 ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟 ");
        }
        if (remainingSeconds > 0) {
            sb.append(remainingSeconds).append("秒");
        }

        return sb.toString().trim();
    }
}
