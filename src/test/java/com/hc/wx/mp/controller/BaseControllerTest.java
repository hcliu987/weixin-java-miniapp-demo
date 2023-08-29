package com.hc.wx.mp.controller;

import com.hc.wx.mp.config.LotteryProperties;
import com.hc.wx.mp.config.RedisCache;
import com.hc.wx.mp.entity.User;
import com.hc.wx.mp.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 公共测试方法和参数.
 *
 * @author Binary Wang
 * @date 2019-06-14
 */
@SpringBootTest

@RunWith(SpringRunner.class)
public  class BaseControllerTest {



    @Autowired
    RedisCache redisCache;

    @Autowired
    Task task;
    @Autowired
    LotteryProperties lotteryProperties;

    @Test
    public  void test() throws InterruptedException {
//        List<User> iUsers = new ArrayList<>();
//        User iUser1 = new User();
//        iUser1.setDeviceId("58b76b28-9f86-471c-8a29-28bf4db6990a");
//        iUser1.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTY5Mzk4ODQzMSwidXNlcklkIjoxMTI5NTI5MTYwLCJkZXZpY2VJZCI6IjU4Yjc2YjI4LTlmODYtNDcxYy04YTI5LTI4YmY0ZGI2OTkwYSIsImlhdCI6MTY5MTM5NjQzMX0.8uNtD7wTzUDHduFbsMzcuFl3vzxKq3qJDAqMkgwFTto");
//        User iUser2 = new User();
//        iUser2.setDeviceId("e4fcc820-6551-4f63-a19a-392bf0a664e0");
//        iUser2.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTY5MzgyMzQ5NywidXNlcklkIjoxMDkzMjczMjM2LCJkZXZpY2VJZCI6ImU0ZmNjODIwLTY1NTEtNGY2My1hMTlhLTM5MmJmMGE2NjRlMCIsImlhdCI6MTY5MTIzMTQ5N30.M45cKhg8XnSkZUhQA7HWB2Xzo9YyLbVizo0TDxIfQyM");
//        User iUser3 = new User();
//        iUser3.setDeviceId("2597a2ad-3ae4-4fad-807d-2933d6d7967d");
//        iUser3.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTY5MzkwMTc5OCwidXNlcklkIjoxMTI4NDI1Nzg0LCJkZXZpY2VJZCI6IjI1OTdhMmFkLTNhZTQtNGZhZC04MDdkLTI5MzNkNmQ3OTY3ZCIsImlhdCI6MTY5MTMwOTc5OH0.2tNegK0Fm-y0x2KWWxajl0BGZr0vojKYo0KcpcDIZG8");
//        User iUser4 = new User();
//        iUser4.setDeviceId("98c1a75b-f7e3-42db-92af-faec533f0c39");
//        iUser4.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTY5NTc3Nzk0MSwidXNlcklkIjoxMTI3MjkwMDAyLCJkZXZpY2VJZCI6Ijk4YzFhNzViLWY3ZTMtNDJkYi05MmFmLWZhZWM1MzNmMGMzOSIsImlhdCI6MTY5MzE4NTk0MX0.jXrSnokJ4KnBUQ5Qy1ej0iUdrANxqn6zcqOn4JQEEQQ");
//        iUsers.add(iUser1);
//        iUsers.add(iUser2);
//        iUsers.add(iUser3);
//        iUsers.add(iUser4);
//
//
//        redisCache.setCacheList("user_list",iUsers);


    }


    @Test
    public  void testN(){

        task.appointmentResults();

    }
}
