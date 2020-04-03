package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/8 13:02
 * @Version 1.0
 **/
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/login")
    public String login(String email, String password) {
/*        //判断用户名密码是否为空
        if (email == null || password == null) {
            return "forward:/login.jsp";
        }
        //根据email查询数据库
        User user = userService.login(email);
        //判断返回结果是否为空
        if (user != null) {
            //判断密码是否正确
            if (password.equals(user.getPassword())) {

                List<Module> modules = moduleService.findModuleByUserId(user.getUserId());
                session.setAttribute("modules", modules);
                session.setAttribute("loginUser", user);
                return "home/main";
            }
        }
        //登陆失败返回error信息
        session.setAttribute("error", "用户名或者密码错误!");
        return "forward:/login.jsp";
    */

        /**
         * 使用shiro进行登录验证，权限控制
         *
         */
        //获取subject
        try {
            Subject subject = SecurityUtils.getSubject();
            //构造用户登录密码
            UsernamePasswordToken upToken = new UsernamePasswordToken(email,password);
            //借助subject 进行登录
            subject.login(upToken);
            //获得用户对象
            User user = (User) subject.getPrincipal();
            //放到会话域中
            session.setAttribute("loginUser",user);
            //查询数据库获得动态菜单
            List<Module> modules = moduleService.findModuleByUserId(user.getUserId());
            //把菜单放到会话域中
            session.setAttribute("modules",modules);
            return "home/main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            session.setAttribute("error", "用户名或者密码错误!");
            return "forward:/login.jsp";
        }
    }

    @RequestMapping("logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        session.removeAttribute("loginUser");
        //session.invalidate();
        return "forward:/login.jsp";
    }


    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }
}
