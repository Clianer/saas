package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/11 16:11
 * @Version 1.0
 **/
public interface RoleDao {
    //根据企业id查询全部
    List<Role> findAll(String CompanyId);
    //根据id查询
    Role findById(String id);
    //根据id删除
    void delete(String id);
    //保存
    void add(Role role);
    //更新
    void upData(Role role);
    //根据ID查询用户角色是否存在
    Long findRoleModuleByRoleId(String id);
    //删除该用户目前所拥有的权限
    void deleteRoleModule(String id);
    //添加权限
    void saveRoleModule(String id,String moduleIds);
    //查询用户所拥有的角色
    List<Role> findRoleByUser(String userId);
}
