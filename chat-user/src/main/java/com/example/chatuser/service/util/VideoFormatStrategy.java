package com.example.chatuser.service.util;

import java.util.HashMap;
import java.util.Map;

public class VideoFormatStrategy {

    private static final Map<String, VideoFormat> VIDEO_FORMAT_MAP = new HashMap<>();

    static {
        VIDEO_FORMAT_MAP.put("avi", VideoFormat.AVI);
        VIDEO_FORMAT_MAP.put("mp4", VideoFormat.MP4);
        VIDEO_FORMAT_MAP.put("mov", VideoFormat.MOV);
        VIDEO_FORMAT_MAP.put("wmv", VideoFormat.WMV);
        VIDEO_FORMAT_MAP.put("flv", VideoFormat.FLV);
    }

    public static VideoFormat getVideoFormat(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length == 0) {
            return VideoFormat.UNKNOWN;
        }

        String extension = parts[parts.length - 1].toLowerCase();
        return VIDEO_FORMAT_MAP.getOrDefault(extension, VideoFormat.UNKNOWN);
    }

    public enum VideoFormat {
        AVI,
        MP4,
        MOV,
        WMV,
        FLV,
        UNKNOWN
    }
}
