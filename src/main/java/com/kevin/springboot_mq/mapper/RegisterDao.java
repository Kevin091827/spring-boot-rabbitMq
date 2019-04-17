package com.kevin.springboot_mq.mapper;

import com.kevin.springboot_mq.entity.User;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 增加user
     * @param user
     * @return
     */
    @Insert("insert into user_tb (_userName,_sex)values(#{user.userName},#{user.sex})")
    void addUser(@Param("user") User user);

    /**
     * 删除user
     * @param id
     * @return
     */
    @Delete("delete from user_tb where _id = #{id}")
    void deleteUser(int id);


}
