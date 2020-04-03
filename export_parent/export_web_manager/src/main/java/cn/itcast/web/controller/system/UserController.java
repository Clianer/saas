package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/11 16:30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    //用户列表分页
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int PageSize) {
        //0. 添加权限
        //0.1 获得Subject对象
        //Subject subject = SecurityUtils.getSubject();
        //subject.checkPermission("用户管理");
        //1.调用service查询用户列表
        PageInfo<User> pageInfo =
                userService.findByPage(getLoginCompanyId(), pageNum, PageSize);
        //2.将用户列表保存到request域中
        request.setAttribute("pageInfo", pageInfo);
        //3.跳转到对象的页面
        return "system/user/user-list";
    }

    /**
     * 进入新增用户页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        String companyId = getLoginCompanyId();
        //1.查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);
        //2.存入request域
        request.setAttribute("deptList", deptList);
        return "system/user/user-add";
    }

    /**
     * 新增或更新用户
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RequestMapping("/edit")
    public String edit(User user) {
        String company = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        user.setCompanyId(company);
        user.setCompanyName(companyName);

        //1.判断是否具有id属性
        if (StringUtils.isEmpty(user.getUserId())) {
            //2.用户id为空，执行保存
            userService.save(user);
            //往rabbitMQ存放信息
            if (user.getEmail() != null && user.getEmail() != "") {
                String to = user.getEmail();
                String subject = "大佬入职通知";
                String content = "欢迎你的到来";

                //发送信息
                HashMap<String, String> map = new HashMap<>();
                map.put("to",to);
                map.put("subject",subject);
                map.put("content",content);
                //
                rabbitTemplate.convertAndSend("msg.email",map);
            }


        } else {
            //3.用户id不为空，执行更新
            userService.update(user);
        }
        return "redirect:/system/user/list";
    }

    /**
     * 进入到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        User user = userService.findById(id);
        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("user", user);
        request.setAttribute("deptList", deptList);
        return "system/user/user-update";
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Integer> delete(String id) {
        Map<String, Integer> result = new HashMap<>();
        boolean flag = userService.delete(id);
        if (flag) {
            result.put("msg", 1);
        } else {
            result.put("msg", 2);
        }
        //跳转到修改界面
        return result;
    }

    /**
     * 地址：/system/user/roleList.do
     * 用户角色显示
     * 进入用户角色页面
     */
    @RequestMapping("roleList")
    public String roleList(String id) {
        //先根据ID查询用户
        User user = userService.findById(id);
        //查询所有角色
        List<Role> roleList = roleService.findAll(getLoginCompanyId());
        //根据该用户Id查询所拥有的角色是什么
        List<Role> userRoles = roleService.findRoleByUser(id);
        //遍历用户所拥有的角色进行拼接返回到前端再次进行遍历让他默认选中
        String userRoleStr = "";
        for (Role userRole : userRoles) {
            userRoleStr += userRole.getId() + ",";
        }
        //放到请求域
        request.setAttribute("user", user);
        request.setAttribute("roleList", roleList);
        request.setAttribute("userRoleStr", userRoleStr);
        return "system/user/user-role";
    }

    /**
     * 地址：/system/user/changeRole.do
     * 保存用户角色
     */
    @RequestMapping("changeRole")
    public String changeRole(String userId, String[] roleIds) {
        userService.updataUserRole(userId, roleIds);
        return "redirect:/system/user/list";
    }
}
