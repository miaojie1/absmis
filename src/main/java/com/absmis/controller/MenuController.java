package com.absmis.controller;

import com.absmis.domain.authority.User;
import com.absmis.service.authority.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuController {

    @Autowired
    UserService userService;

    //菜单页面
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        System.out.print("controller");
        return "index";
    }
//
//    @ResponseBody
//    @PostMapping("/toLogin")
//    public String toLogin(User user) {
//        User mUser = userService.findByUsername(user.getUsername());
//        if (mUser.getPassword() == user.getPassword()) {
//            //成功
//        } else {
//            //失败
//        }
//        return null;
//    }
}
