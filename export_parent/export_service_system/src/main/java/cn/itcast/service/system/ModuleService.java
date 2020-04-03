package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/11 16:20
 * @Version 1.0
 **/
public interface ModuleService {
    // 分页查询
    PageInfo<Module> findByPage(int pageNum, int pageSize);

    //查询所有部门
    List<Module> findAll();

    //保存
    void save(Module user);

    //更新
    void update(Module user);

    //删除
    void delete(String id);

    //根据id查询
    Module findById(String id);
    //根据角色ID查找权限
    List<Module> findModuleByRoleId(String id);
    //查找用户拥有什么权限
    List<Module> findModuleByUserId(String userId);
}
