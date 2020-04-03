package cn.itcast.web.controller.system;

import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author han
 * @Date 2020/3/12 19:05
 * @Version 1.0
 **/
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {
        //1.调用service查询部门列表
        PageInfo pageInfo = sysLogService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        //2.将部门列表保存到request域中
        request.setAttribute("pageInfo",pageInfo);
        //3.跳转到对象的页面
        return "system/log/log-list";
    }

}
