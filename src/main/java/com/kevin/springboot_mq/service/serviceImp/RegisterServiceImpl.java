package com.kevin.springboot_mq.service.serviceImp;

import com.kevin.springboot_mq.entity.User;
import com.kevin.springboot_mq.mapper.RegisterDao;
import com.kevin.springboot_mq.message.Consumer;
import com.kevin.springboot_mq.message.Producer;
import com.kevin.springboot_mq.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/16 22:06
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/16 22:06
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Transactional
@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private Producer producer;

    @Override
    public void addUser(String userName) {
        producer.sendObject(userName);
    }
}
