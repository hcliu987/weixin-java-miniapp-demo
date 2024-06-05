package com.hc.wx.mp.controller;

import com.hc.wx.mp.entity.SFUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sf/user")
@Api("用户管理")
public class SFController {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/add")
    @ApiOperation(value = "添加顺丰用户")
    public void addUser(@RequestBody SFUser user) {
        redisTemplate.opsForValue().set("sf:" + user.getPhone(), user);
    }

    @PostMapping("up")
    @ApiOperation(value = "青龙用户token更新")
    public void up(@RequestParam("auth") String auth) {
        redisTemplate.opsForValue().set("auth", auth);
    }
}
