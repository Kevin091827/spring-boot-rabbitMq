package com.kevin.springboot_mq.controller;

import com.kevin.springboot_mq.entity.User;
import com.kevin.springboot_mq.message.Producer;
import com.kevin.springboot_mq.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:    DOTO
 * @Author:         Kevin
 * @CreateDate:     2019/4/11 13:55
 * @UpdateUser:     Kevin
 * @UpdateDate:     2019/4/11 13:55
 * @UpdateRemark:   修改内容
 * @Version: 1.0
 */
@Controller
@ResponseBody
public class MsgController {

    @Autowired
    private Producer producer;

    @Autowired
    private RegisterService registerService;

    /**
     * 发送消息
     * @return
     */
    @GetMapping("/helloRabbitMQ")
    public String sendMsg(){
        User user = new User("kevin",12,"M");
        //registerService.addUser(user);
        return "success";
    }

}
