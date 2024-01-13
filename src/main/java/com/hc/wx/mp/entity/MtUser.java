package com.hc.wx.mp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class MtUser {
    private String name;
    private String userId;
    private String token;
    private String deviceId;
}
