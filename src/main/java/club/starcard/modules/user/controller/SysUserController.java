package club.starcard.modules.user.controller;

import club.starcard.modules.user.entity.SysUser;
import club.starcard.modules.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhuzhiou
 */
@RequestMapping("/admin/user")
@Controller
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping(path = "/list")
    public String list(Model model) {
        List<SysUser> sysUsers = sysUserService.getSysUsers();
        model.addAttribute("sysUsers", sysUsers);
        return "user/user_list";
    }

    @GetMapping(path = "/form")
    public String form(@RequestParam(required = false) Long userid, Model model) {
        SysUser sysUser;
        if (userid != null) {
            sysUser = sysUserService.getSysUserByUserid(userid);
        } else {
            sysUser = new SysUser();
        }
        model.addAttribute("sysUser", sysUser);
        return "user/user_form";
    }

    @PostMapping(path = "/user/create")
    public String create(SysUser sysUser) {
        sysUserService.saveSysUser(sysUser);
        return "redirect:user/user_detail";
    }

    @PostMapping(path = "/user/update")
    public String update(SysUser sysUser) {
        return "redirect:user/user_detail";
    }

    @GetMapping(path = "/user/detail")
    public String detail(@RequestParam String userid) {
        return "user/user_detail";
    }
}
