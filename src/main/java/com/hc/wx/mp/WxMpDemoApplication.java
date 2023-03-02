package com.hc.wx.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@SpringBootApplication
@EnableScheduling
public class WxMpDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(WxMpDemoApplication.class, args);
  }
}
