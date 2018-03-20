package club.starcard.modules.member.controller;

import club.starcard.config.CommonConfig;
import club.starcard.modules.member.entity.Group;
import club.starcard.modules.member.entity.Member;
import club.starcard.modules.member.service.GroupService;
import club.starcard.modules.member.service.InviteService;
import club.starcard.modules.member.service.MemberService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequestMapping("/member/")
@Controller
public class MemberController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CommonConfig config;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 新增会员界面
     *
     * @return
     */
    @GetMapping("add")
    public String addView(Model model) {
        model.addAttribute("banks", config.getBanks());
        model.addAttribute("member", new Member());
        return "crm/member-add";
    }

    /**
     * 开盘新增顶级会员
     *
     * @param model
     * @return
     */
    @GetMapping("open")
    public String openView(Model model) {
        model.addAttribute("open", true);
        model.addAttribute("banks", config.getBanks());
        model.addAttribute("member", new Member());
        return "crm/member-add";
    }

    @GetMapping("edit/{memberId}")
    public String editView(Model model, @PathVariable Long memberId) {
        model.addAttribute("member", memberService.get(memberId));
        model.addAttribute("banks", config.getBanks());
        return "crm/member-add";
    }

    @PostMapping("add")
    @ResponseBody
    public JSONObject add(@ModelAttribute Member member) {
        JSONObject result = new JSONObject();
        boolean flag;
        if (member.getParentId() != null) {
            //添加会员
            flag = inviteService.qualifying(member);
        } else {
            //开盘
            flag = inviteService.crmOpen(member);
        }
        if (flag) {
            result.put("code", 0);
        }
        return result;
    }

    @PostMapping("edit")
    @ResponseBody
    public JSONObject edit(@ModelAttribute Member member) {
        JSONObject result = new JSONObject();
        if (memberService.updateSelection(member)) {
            result.put("code", 0);
        }
        return result;
    }

    public JSONObject update(@ModelAttribute Member member) {
        if (member.getMemberId() != null) {

        }

        return null;
    }

    @GetMapping("list")
    public String listView() {
        return "crm/member-list";
    }

    @PostMapping("list")
    @ResponseBody
    public Page<Member> list(String dateMin, String dateMax, String search,
                             Integer pageSize, Integer pageNum) {
        Date beginDate = null;
        Date endDate = null;
        try {
            if (StringUtils.isNotBlank(dateMin)) {
                beginDate = sdf.parse(dateMin);
            }
            if (StringUtils.isNotBlank(dateMax)) {
                endDate = sdf.parse(dateMax);
            }
        } catch (Exception e) {
            logger.error("转换日期格式异常，继续查询", e);
        }

        Page<Member> page = memberService.page(beginDate, endDate, search, pageNum, pageSize);
        return page;
    }

    @GetMapping("listGroup")
    public String listGroupView() {
        return "crm/group-list";
    }

    @PostMapping("listGroup")
    @ResponseBody
    public Page<Group> listGroup(String search,Integer pageSize, Integer pageNum) {
        Page<Group> page = groupService.page(search, pageNum, pageSize);
        return page;
    }

    @GetMapping("detail/{memberId}")
    public String detail(Model model, @PathVariable Long memberId) {
        //用户详情
        model.addAttribute("member",memberService.get(memberId));
        //积分消费记录详情
        model.addAttribute("pointLogs",memberService.findPointLogs(memberId));
        //查询下线
        model.addAttribute("childrenList",memberService.findByParentId(memberId));

        return "crm/member-detail";
    }

    @GetMapping("detailGroup/{groupId}")
    public String detailGroup(Model model, @PathVariable Long groupId) {
        //分组详情
        model.addAttribute("group",groupService.get(groupId));
        //查询下线
        model.addAttribute("memberList",memberService.findByGroupId(groupId));

        return "crm/group-detail";
    }
}
