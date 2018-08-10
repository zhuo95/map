package com.zz.map.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceVo {

    private String id;

    private String placeName;

    private Double latitude;

    private Integer eventNum;

    private Double longitude;

    private Date createTime;

    private Date updateTime;

    private List<String> eventTitles;
}
