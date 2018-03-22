package club.starcard.modules.user.controller;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.service.SysUserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhuzhiou
 */
@RequestMapping("/user")
@Controller
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping(path = "/list")
    public String listPage(Model model) {
        //List<SysUser> sysUsers = sysUserServie.getSysUsers();
        //model.addAttribute("sysUsers", sysUsers);
        return "user/user_list";
    }

    @PostMapping(path = "/list")
    @ResponseBody
    public Page<SysUser> listData(@PageableDefault(size = 15) Pageable pageable) {
        Page<SysUser> sysUsers = sysUserService.getAllSysUsers(pageable);
        return sysUsers;
    }

    @GetMapping(path = "/form")
    public String form() {
        return "user/user_form";
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public JSONObject create(SysUser sysUser) {
        JSONObject result = new JSONObject();
        if (StringUtils.length(sysUser.getUsername()) != 11) {
            return result.fluentPut("code", 1).fluentPut("message", "手机号不正确");
        }
        if (sysUserService.getSysUserByUsername(sysUser.getUsername()) != null) {
            return result.fluentPut("code", 2).fluentPut("message", "用户已存在");
        }
        sysUser.setEnabled(true);
        sysUserService.saveSysUser(sysUser);
        return result.fluentPut("code", 0);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public JSONObject delete(@RequestParam long id) {
        JSONObject result = new JSONObject();
        try {
            sysUserService.deleteSysUser(id);
            return result.fluentPut("code", 0);
        } catch (Exception e) {
            return result.fluentPut("code", 1).fluentPut("message", e.getMessage());
        }
    }
}
