package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * @Author han
 * @Date 2020/3/7 19:16
 * @Version 1.0
 **/
@Service(timeout = 100000)
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    //分页查询
    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Company> list = companyDao.findAll();
        return new PageInfo<>(list);
    }

    //查询所有企业
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    //插入一家企业
    @Override
    public void add(Company company) {
        company.setId(UUID.randomUUID().toString());
        companyDao.add(company);
    }

    //更新一家企业信息
    @Override
    public void upData(Company company) {
        companyDao.upData(company);
    }

    //根据ID查找一条记录
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    //删除一家企业
    @Override
    public void del(String id) {
        companyDao.del(id);
    }
}
