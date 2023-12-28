package com.example.chatuser.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BCryptUtil {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public String bCryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean matches(String password, String encryption) {
        return bCryptPasswordEncoder.matches(password, encryption);
    }
}
