package com.zz.map.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    private Long id;

    private Long placeId;

    private Long userId;

    private String userName;

    private String address;

    private Integer category;

    private Double longitude;

    private Double latitude;

    private String description;

    private Date createTime;

    private Integer expireDays;

    private Date expireTime;

    private Date updateTime;

}
