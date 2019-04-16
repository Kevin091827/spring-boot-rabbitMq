package com.kevin.springboot_mq.mapper;

import com.kevin.springboot_mq.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/16 20:53
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/16 20:53
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Mapper
@Repository
public interface RegisterDao {

    @Insert("insert into user_tb (_userName)values(#{userName})")
    int addUser(String userName);

}
