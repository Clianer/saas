package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/7 17:56
 * @Version 1.0
 **/
public interface CompanyDao {
    //查询所有企业
    List<Company> findAll();
    //添加一家企业
    void add(Company company);
    //更新一家企业信息
    void upData(Company company);
    //根据ID查找一条记录
    Company findById(String id);
    //删除一家企业记录
    void del(String id);
}
