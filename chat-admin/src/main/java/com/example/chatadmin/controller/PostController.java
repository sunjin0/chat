package com.example.chatadmin.controller;


import com.example.chatadmin.entity.Post;
import com.example.chatadmin.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-11-09
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;


    @GetMapping("/getPostList/{current}")
    public ResponseEntity<String> getPostList(@PathVariable Integer current) {
        return new ResponseEntity<>(postService.obtainPostList(current), HttpStatus.OK);
    }

    @PostMapping("/changePostInformation")
    public ResponseEntity<String> changeUserInformation(@RequestBody Post post) {
        return new ResponseEntity<>(postService.changePostInformation(post), HttpStatus.OK);
    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        return new ResponseEntity<>(postService.deletePost(postId), HttpStatus.OK);
    }

    @GetMapping("/searchPost/{postId}/{userId}/{current}")
    public ResponseEntity<String> searchPost(@PathVariable String postId, @PathVariable String userId, @PathVariable Integer current) {
        return new ResponseEntity<>(postService.searchPost(postId, userId, current), HttpStatus.OK);
    }

    @GetMapping("/visualizingData")
    public ResponseEntity<String> visualizingData() {
        return new ResponseEntity<>(postService.visualizingData(), HttpStatus.OK);
    }

}
