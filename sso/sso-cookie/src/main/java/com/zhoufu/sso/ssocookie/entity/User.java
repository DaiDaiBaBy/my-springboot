package com.zhoufu.sso.ssocookie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 11:49
 * @description: 用户实体，使用jpa 操作数据库
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Column(name = "uname", length = 100)
    private String uname;

    @Column(name = "upassword", length = 100)
    private String upassword;
}
