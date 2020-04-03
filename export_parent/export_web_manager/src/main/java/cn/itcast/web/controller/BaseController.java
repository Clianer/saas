package cn.itcast.web.controller;

import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author han
 * @Date 2020/3/10 16:32
 * @Version 1.0
 **/
public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;



    //从session中获取登陆用户对象
    protected User getLoginUser(){
        return (User) session.getAttribute("loginUser");
    }
    //获取企业id
    protected String getLoginCompanyId() {
        //用户登录后，获取登录用户所在的企业id
        return getLoginUser().getCompanyId();
    }
    //获取企业名称
    protected String getLoginCompanyName() {
        return getLoginUser().getCompanyName();
    }
}
