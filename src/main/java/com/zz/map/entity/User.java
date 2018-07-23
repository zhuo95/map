package com.zz.map.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uid;

    private Integer gender;

    private String avatar;

    private String username;

    private String password;

    private String email;

    //临时记录，没有啥用
    private String token;

    private Date createTime;

    private Date updateTime;

}
