package com.example.chatadmin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptUtil {


    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BCryptUtil(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String bCryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean matches(String password, String encryption) {
        return bCryptPasswordEncoder.matches(password, encryption);
    }
}
