package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Author han
 * @Date 2020/3/11 16:20
 * @Version 1.0
 **/
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    //分页
    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> list = roleDao.findAll(companyId);
        return new PageInfo<>(list);
    }
    //查询所有角色
    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }
    //保存
    @Override
    public void save(Role role) {
        role.setId(UUID.randomUUID().toString());
        roleDao.add(role);
    }
    //更新
    @Override
    public void update(Role role) {
        roleDao.upData(role);
    }
    //删除
    @Override
    public boolean delete(String id) {
        Long count = roleDao.findRoleModuleByRoleId(id);
        if (count == null || count == 0) {
            roleDao.delete(id);
            return true;
        } else {
            System.out.println("删除失败");
            return false;
        }
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }
    //修改权限
    @Override
    public void updateRoleModule(String id, String moduleIds) {
        //先删除目前所用的权限
        roleDao.deleteRoleModule(id);
        //先判断数组是否为NULL
        if (moduleIds != null && moduleIds != "") {
            //分割获取到的节点ID
            String[] moduleId = moduleIds.split(",");
                //遍历循环添加节点
                for (String s : moduleId) {
                    roleDao.saveRoleModule(id,s);
            }
        }
    }

    @Override
    public List<Role> findRoleByUser(String userId) {
        return roleDao.findRoleByUser(userId);
    }
}
