package cn.itcast.web.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现登录认真
 *
 * @Author han
 * @Date 2020/3/14 15:25
 * @Version 1.0
 **/
public class AuthRealm extends AuthorizingRealm {

    //注入User对象
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //获取用户输入的用户对象
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //获取用户输入的账号
        String email = upToken.getUsername();
        //调用数据库根据邮箱查询账号
        User user = userService.login(email);
        //判断是否为空
        if(user != null){
            SimpleAuthenticationInfo sia = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            return sia;
        }
        return null;
    }

    // 授权访问校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获得用户对象
        User user = (User) principalCollection.getPrimaryPrincipal();
        //获得用户所拥有的权限
        List<Module> modules = moduleService.findModuleByUserId(user.getUserId());
        //构造构造SimpleAuthorizationInfo对象返回
        Set<String> permissions = new HashSet<>();
        //循环遍历所有权限
        for (Module module : modules) {
            //添加到set集合中
            permissions.add(module.getName());
        }
        SimpleAuthorizationInfo sia = new SimpleAuthorizationInfo();
        sia.addStringPermissions(permissions);
        return sia;
    }
}
