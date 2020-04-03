package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/10 2:35
 * @Version 1.0
 **/
public interface DeptService {
    /**
     * 查询所有部门信息并且分页
     */
    PageInfo<Dept> findByPage(String companyId , int pageNum,int pageSize);


    /**
     * 查询所有部门信息
     */
    List<Dept> findAll(String companyId);

    /**
     * 添加
     * @param dept
     */
    void add(Dept dept);

    /**
     * 更新
     * @param dept
     */
    void updata(Dept dept);
    //通过ID查询一个部门
    Dept findById(String id);
    //查询所有部门信息（名称）但不包括自己
    List<Dept> findAllDept(String companyId, String deptId);
    //根据ID删除一条记录
    boolean del(String id);
    //批量删除
    //Integer delMany(String[] ids);
}
