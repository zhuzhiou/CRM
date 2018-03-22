package club.starcard.modules.member.controller;

import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.service.InviteService;
import club.starcard.modules.member.service.MemberService;
import club.starcard.modules.member.vo.MemberVo;
import club.starcard.modules.user.entity.SysUser;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * Created by wlrllr on 2018/3/15.
 * web入口，包含：1，系统顶级会员初始化初始化
 * 2，推荐用户
 */
@RequestMapping("/")
@Controller
public class CommonController {

    @Autowired
    private MemberService memberService;

    @GetMapping("index")
    public String index(Model model) {
        //获取登录用户信息
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("sysUser",user);
        //获取菜单栏

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(0,20,sort);
        Page<Member> page = memberService.page(new Member(),pageable);
        if(page != null && page.getTotalElements()>0){
            model.addAttribute("memberList",page.getContent());
        }else{
            model.addAttribute("memberList",new ArrayList<>());
        }
       return "crm/index";
    }

    /**
     * 测试页面用
     * @param pageName
     * @return
     */
    @GetMapping("page/{pageName}")
    public String init(@PathVariable String pageName) {
        return "crm/"+pageName;
    }

}
