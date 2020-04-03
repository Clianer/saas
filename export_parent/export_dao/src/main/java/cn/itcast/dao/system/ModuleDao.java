package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);
    //根据id删除
    void delete(String moduleId);
    //添加
    void save(Module module);
    //更新
    void update(Module module);
    //查询全部
    List<Module> findAll();
    //根据角色ID查询所拥有的权限
    List<Module> findModuleByRoleId(String id);
    //查询用户所拥有的权限
    List<Module> findModuleByUserId(String userId);
    //查询权限等级
    List<Module> findByBelong(int belong);
}