package club.starcard.modules.member.controller;

import club.starcard.config.CommonConfig;
import com.alibaba.fastjson.JSONObject;
import club.starcard.modules.member.service.InviteService;
import club.starcard.modules.member.vo.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wlrllr on 2018/3/15.
 * web入口，包含：1，系统顶级会员初始化初始化
 * 2，推荐用户
 */
@RequestMapping("/")
@Controller
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private InviteService inviteService;



    @GetMapping("index")
    public String index() {
       return "crm/index";
    }

    @GetMapping("")
    public String login() {
        return "login";
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

    @RequestMapping("invite")
    public JSONObject invite(MemberVo vo) {
        JSONObject result = new JSONObject();
        if(inviteService.qualifying(vo.getMember())){
            result.put("code",0);
        }
        return result;
    }
}
