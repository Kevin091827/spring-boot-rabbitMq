package com.kevin.springboot_mq.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/16 20:55
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/16 20:55
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private String userName;
    private int id;
    private String sex;

    public User(String userName, int id, String sex) {
        this.userName = userName;
        this.id = id;
        this.sex = sex;
    }
}
