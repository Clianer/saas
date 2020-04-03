package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 工厂模块
 */
public interface FactoryService {

    /**
     * 分页查询
     */
    PageInfo<Factory> findByPage(
            FactoryExample factoryExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Factory> findAll(FactoryExample factoryExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Factory findById(String id);

    /**
     * 新增
     */
    void save(Factory factory);

    /**
     * 修改
     */
    void update(Factory factory);

    /**
     * 删除部门
     */
    void delete(String id);

    String findFactoryNameById(String factoryName);
}











