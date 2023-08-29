package com.hc.wx.mp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class User {

    private String token;
    private String deviceId;
}
