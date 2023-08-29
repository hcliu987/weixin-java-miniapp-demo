package com.hc.wx.mp.controller;

import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mt/user")
@Api("用户接口")
public class UserController {


    @Autowired
    RedisCache redisCache;
    @PostMapping("/add")
    @ApiOperation( value = "添加茅台通知用户")
    public  void addUser(@RequestBody List<User> users){
        redisCache.setCacheList("user_list",users);
    }

}
