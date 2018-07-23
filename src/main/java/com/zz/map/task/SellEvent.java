package com.zz.map.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SellEvent {
    private String address;

    private String username;

    private Long uid;

    private Date createTime;

    private String subject;

    private String url;

    private Long tid;
}
