package com.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member/")
@Controller
public class MemberController {

    @GetMapping("add")
    public String addView() {
        return "crm/member-add";
    }

    @PostMapping("add")
    public JSONObject add() {
        JSONObject result = new JSONObject();
        result.put("code",0);
        return result;
    }

    @GetMapping("list")
    public String listView() {
        return "crm/member-list";
    }
}
