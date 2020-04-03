package cn.itcast.web.stat;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/22 20:46
 * @Version 1.0
 **/
@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {
    @Reference
    private StatService statService;

    /**
     * 进入统计分析页面
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+chartsType;
    }

    /**
     * 生产厂家销售统计, 页面异步请求返回json格式数据
     */
    @RequestMapping("/getFactoryData")
    @ResponseBody
    public List<Map<String, Object>> getFactoryData(){
        List<Map<String, Object>> list = statService.getFactoryData();
        return list;
    }

}
