package com.zz.map.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    private Long userId;

    private String userName;

    private String address;

    private Integer category;

    private Double longitude;

    private Double latitude;

    private String description;

    //活动日期
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date;

    private Date createTime;

    //用于计算过期时间
    private Integer expireDays;

    //CLOSE/OPEN
    private Integer status;

    //计算的expireTime
    private Date expireTime;

    private Date updateTime;

}
