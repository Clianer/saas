package cn.itcast.dao.stat;

import cn.itcast.domain.company.Company;

import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/22 17:21
 * @Version 1.0
 **/
public interface StatDao {
    //生产厂家销售统计
    List<Map<String,Object>> getFactoryData();
}
