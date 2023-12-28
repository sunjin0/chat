package com.example.chatadmin.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information implements Serializable {
    private String Tag;
    private String Message;

    private static Information create(String tag, String message) {
        return new Information(tag, message);
    }

    public static String toString(String tag, String message) {
        return JSON.toJSONString(create(tag, message));
    }
}
