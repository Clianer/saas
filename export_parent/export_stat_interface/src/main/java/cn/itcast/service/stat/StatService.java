package cn.itcast.service.stat;

import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/22 21:00
 * @Version 1.0
 **/
public interface StatService {
    /**
     * 根据厂家统计
     */
    List<Map<String,Object>> getFactoryData();
}
