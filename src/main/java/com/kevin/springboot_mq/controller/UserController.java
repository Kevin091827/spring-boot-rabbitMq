package com.kevin.springboot_mq.controller;

import com.kevin.springboot_mq.entity.User;
import com.kevin.springboot_mq.message.Producer;
import com.kevin.springboot_mq.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PreDestroy;

/**
 * @Auther: Kevin
 * @Date:
 * @ClassName:UserController
 * @Description: TODO
 */
@Controller
@ResponseBody
@Slf4j
public class UserController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private Producer producer;

    @GetMapping("/registerUser")
    public String registerUser(){
        for(int i = 19;i<600;i++){
            registerService.addUser(new User("kevin",i,"M"));
        }
        return "success";
    }

    @GetMapping("/deleteUser")
    public String deleteUser() throws InterruptedException {
        for (int i = 1290;i<1300;i++){
            //Thread.sleep(1000);
            registerService.deleteUser(i);
        }
        return "success";
    }
}
