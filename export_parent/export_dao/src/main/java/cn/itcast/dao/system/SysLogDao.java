package cn.itcast.dao.system;

import cn.itcast.domain.system.SysLog;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/12 19:00
 * @Version 1.0
 **/
public interface SysLogDao {
    //查询全部
    List<SysLog> findAll(String companyId);

    //添加
    void save(SysLog log);
}
