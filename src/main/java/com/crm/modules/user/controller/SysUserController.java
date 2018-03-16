package com.crm.modules.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhuzhiou
 */
@RequestMapping("/admin/user")
public class SysUserController {

    @GetMapping(path = "/user_list")
    public String usersManager() {
        return "user/user_list";
    }

    @GetMapping(path = "/user_form")
    public String userEdit(@RequestParam(required = false) String userid) {
        return "redirect: user/user_form";
    }

    @GetMapping(path = "/user_detail")
    public String userDetail(@RequestParam String userid) {
        return "redirect: user/user_detail";
    }
}
