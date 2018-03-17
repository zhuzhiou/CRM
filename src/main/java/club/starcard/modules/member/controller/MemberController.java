package club.starcard.modules.member.controller;

import com.alibaba.fastjson.JSONObject;
import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.service.InviteService;
import club.starcard.modules.member.service.MemberService;
import club.starcard.modules.member.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member/")
@Controller
public class MemberController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private MemberService memberService;

    /**
     * 新增会员界面
     * @return
     */
    @GetMapping("add")
    public String addView() {
        return "crm/member-add";
    }

    /**
     * 开盘新增顶级会员
     * @param model
     * @return
     */
    @GetMapping("open")
    public String openView(Model model) {
        model.addAttribute("open",true);
        return "crm/member-add";
    }

    @PostMapping("add")
    @ResponseBody
    public JSONObject add(@ModelAttribute MemberVo memberVo) {
        //FIXME 校驗用戶是否已存在
        JSONObject result = new JSONObject();
        boolean flag = false;
        if(memberVo.getParentId() != null){
            //添加会员
            flag = inviteService.qualifying(memberVo.getMember());
        }else{
            //开盘
            flag = inviteService.crmOpen(memberVo);
        }
        if(flag){
            result.put("code",0);
        }
        return result;
    }

    @GetMapping("list")
    public String listView() {
        return "crm/member-list";
    }

    @PostMapping("list")
    @ResponseBody
    public Page<Member> list(@ModelAttribute  Member member,
                                   Integer pageSize,Integer pageNum) {
        //FIXME 如何用example支持like EXample.of()无法满足
        /*search:
        dateMin:
        dateMax:*/
        Page<Member> page = memberService.page(member,pageNum,pageSize);
        return page;
    }
}
