package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/7 18:17
 * @Version 1.0
 **/
public interface CompanyService {

    //分页
    PageInfo<Company> findByPage(int pageNum, int pageSize);
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
