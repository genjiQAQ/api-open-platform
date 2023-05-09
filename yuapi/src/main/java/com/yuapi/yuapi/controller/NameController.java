package com.yuapi.yuapi.controller;

import com.yupi.yuapiclientsdk.model.User;
import com.yupi.yuapiclientsdk.utils.SignUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        return "Get u name is" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post u name is" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        return "u name is" + user.getUsername();

    }



}
