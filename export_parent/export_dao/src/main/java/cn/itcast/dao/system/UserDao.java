package cn.itcast.dao.system;

import cn.itcast.domain.system.User;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/11 16:11
 * @Version 1.0
 **/
public interface UserDao {
    //根据企业id查询全部
    List<User> findAll(String CompanyId);
    //根据id查询
    User findById(String id);
    //根据id删除
    void delete(String id);
    //保存
    void add(User user);
    //更新
    void upData(User user);
    //根据ID查询用户角色是否存在
    Long findUserRoleByUserId(String id);
    //保存用户角色
    void updataUserRole(String userId, String roleId);
    //删除用户角色
    void deleteUserRole(String userId);
    //登陆
    User login(String email);
}
