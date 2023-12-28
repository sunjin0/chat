package com.example.chatuser.controller;


import com.example.chatuser.service.impl.TagsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-10-31
 */
@RestController
@RequestMapping("/user/tags")
public class TagsController {

    @Autowired
    private TagsServiceImpl tagsService;

    /**
     * 获取标签
     *
     * @return {@link String}
     */
    @GetMapping("/getTags")
    public String getTags(@RequestHeader("Authorization") String token) {
        return tagsService.getTags();
    }
}
