package com.zz.map.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    private String id;

    private String placeName;

    private Double latitude;

    private Integer eventNum;

    private Double longitude;

    private Date createTime;

    private Date updateTime;
}
