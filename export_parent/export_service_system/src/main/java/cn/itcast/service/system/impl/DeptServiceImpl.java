package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Author han
 * @Date 2020/3/10 2:36
 * @Version 1.0
 **/
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    @Override
    public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //数据库查找所有部门返回List
        List<Dept> list = deptDao.findAll(companyId);
        //返回结果
        return new PageInfo<Dept>(list);
    }

    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    @Override
    public void add(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        if(dept.getParent() != null && "".equals(dept.getParent().getId())){
            dept.setParent(null);
        }
        deptDao.add(dept);
    }

    @Override
    public void updata(Dept dept) {
        deptDao.updata(dept);
    }

    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    @Override
    public List<Dept> findAllDept(String companyId, String deptId) {
        return deptDao.findAllDept(companyId,deptId);
    }

    @Override
    public boolean del(String id) {
        //先查新该部门存不存在上级部门
       List<Dept> list = deptDao.findParentById(id);
        //如果上级部门为NULL或者长度等于0则调用方法进行删除
        if(list == null || list.size()== 0){
            deptDao.del(id);
            return true;
        }
        return false;
    }

/*    @Override
    public Integer delMany(String[] ids) {
        return deptDao.delMany(ids);
    }*/
}
