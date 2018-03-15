package com.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.crm.service.GroupService;
import com.crm.vo.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by wlrllr on 2018/3/15.
 * web入口，包含：1，系统顶级会员初始化初始化
 * 2，推荐用户
 */
@RequestMapping("/common")
@Controller
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private GroupService groupService;


    @GetMapping("/init")
    public String init() {
       return "";
    }

    @PostMapping("/doInit")
    public JSONObject doInit(@RequestBody List<MemberVo> vo){
        JSONObject result = new JSONObject();
        if(vo != null && vo.size()>0){
            groupService.initData(vo);
        }
        return result;
    }

    @RequestMapping("/invite")
    public JSONObject invite(MemberVo vo) {
        JSONObject result = new JSONObject();
        if(groupService.qualifying(vo.getMember())){
            result.put("code",0);
        }
        return result;
    }
}
