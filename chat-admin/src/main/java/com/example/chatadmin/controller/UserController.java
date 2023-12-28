package com.example.chatadmin.controller;


import com.example.chatadmin.entity.User;
import com.example.chatadmin.service.impl.UserServiceImpl;
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
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
    }

    @GetMapping("/user/visualizingData")
    public ResponseEntity<String> visualizingData() {
        return new ResponseEntity<>(userService.visualizingData(), HttpStatus.OK);
    }

    @GetMapping("/user/getUserList/{current}")
    public ResponseEntity<String> getUserList(@PathVariable Integer current) {
        return new ResponseEntity<>(userService.obtainUserList(current), HttpStatus.OK);
    }

    @PostMapping("/user/changeUserInformation")
    public ResponseEntity<String> changeUserInformation(@RequestBody User user) {
        return new ResponseEntity<>(userService.changeUserInformation(user), HttpStatus.OK);
    }

    @GetMapping("/user/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @GetMapping("/user/searchUser/{username}/{email}/{current}")
    public ResponseEntity<String> searchUser(@PathVariable String username, @PathVariable String email, @PathVariable Integer current) {
        return new ResponseEntity<>(userService.searchUser(username, email, current), HttpStatus.OK);
    }

    @PostMapping("/user/sanctionUser")
    public ResponseEntity<String> sanctionUser(@RequestBody User user) {
        userService.sanctionUser(user);
        return new ResponseEntity<>("true", HttpStatus.OK);
    }

}
