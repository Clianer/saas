package cn.itcast.web.shiro;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 *
 * //自定义凭证匹配器
 * @Author han
 * @Date 2020/3/14 16:08
 * @Version 1.0
 **/
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获得用户输入名
        String email = (String) token.getPrincipal();
        //获得用户输入密码
        String password = new String((char[]) token.getCredentials());
        //加密
        String md5Hash = new Md5Hash(password, email).toString();
        //获得数据库密码
        String dbPwd = (String) info.getCredentials();
        //返回匹配密码结果
        return md5Hash.equals(dbPwd);
    }
}
