package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/10 2:22
 * @Version 1.0
 **/
public interface DeptDao {

    /**
     * 通过查询企业ID查询所有部门
     */
    List<Dept> findAll(String companyId);

    /**
     * 通过ID查询一个部门
     */
    Dept findById(String id);

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

    /**
     * 查询所有部门名称但不包括自己
     * @param companyId
     * @param deptId
     * @return
     */
    List<Dept> findAllDept(String companyId, String deptId);
    //根据ID删除一条信息
    void del(String id);
    //根据父部门ID进行查询
    List<Dept> findParentById(String id);
    //批量删除部门
    //int delMany(String[] ids);
}
