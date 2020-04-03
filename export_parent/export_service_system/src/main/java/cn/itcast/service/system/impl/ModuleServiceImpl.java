package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.UUID;

/**
 * @Author han
 * @Date 2020/3/11 16:20
 * @Version 1.0
 **/
@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private UserService userService;

    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Module> list = moduleDao.findAll();
        return new PageInfo<>(list);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public void save(Module module) {
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public List<Module> findModuleByRoleId(String id) {
        return moduleDao.findModuleByRoleId(id);
    }
    /**
     * 根据登录的用户id查询用户所具有的所有权限（模块，菜单）
     *   1.根据用户id查询用户
     *   2.根据用户degree级别判断
     *   3.如果degree==0 （内部的sass管理）
     *      根据模块中的belong字段进行查询，belong = "0";
     *   4.如果degree==1 （租用企业的管理员）
     *      根据模块中的belong字段进行查询，belong = "1";
     *   5.其他的用户类型
     *      借助RBAC的数据库模型，多表联合查询出结果
     */
    @Override
    public List<Module> findModuleByUserId(String userId) {
        //查询用户
        User user = userService.findById(userId);
        //获取用户的degree等级
        Integer degree = user.getDegree();
        //内部等级
        if(degree == 0){
            return moduleDao.findByBelong(0);
        }
        //管理等级
        else if(degree == 1){
            return moduleDao.findByBelong(1);
        }
        //其他用户等级
        else {
            return moduleDao.findModuleByUserId(userId);
        }

    }
}
