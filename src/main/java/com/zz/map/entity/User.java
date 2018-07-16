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

    private Integer gender;

    private String nickName;

    private String avatar;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String contactInfo;

    @Column(length = 100)
    private String password;

    @Column(length = 100)
    private String question;

    @Column(length = 100)
    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

}
