package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;

/**
 * @Author han
 * @Date 2020/3/12 19:02
 * @Version 1.0
 **/
public interface SysLogService {
    /**
     * 分页
     */
    PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize);

    //保存
    void save(SysLog log);
}
