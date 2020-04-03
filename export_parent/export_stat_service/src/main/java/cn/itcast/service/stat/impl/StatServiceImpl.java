package cn.itcast.service.stat.impl;

import cn.itcast.dao.stat.StatDao;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import cn.itcast.service.stat.StatService;

import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/22 20:42
 * @Version 1.0
 **/
@Service
public class StatServiceImpl implements StatService{

    // 注入dao
    @Autowired
    private StatDao statDao;

    @Override
    public List<Map<String, Object>> getFactoryData() {
        return statDao.getFactoryData();
    }
}
