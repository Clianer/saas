package cn.itcast.service.system;

import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/11 16:20
 * @Version 1.0
 **/
public interface RoleService {
    // 分页查询
    PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize);

    //查询所有部门
    List<Role> findAll(String companyId);

    //保存
    void save(Role role);

    //更新
    void update(Role role);

    //删除
    boolean delete(String id);

    //根据id查询
    Role findById(String id);

    //修改角色权限
    void updateRoleModule(String id, String moduleIds);
    //查询用户拥有的角色
    List<Role> findRoleByUser(String userId);
}
