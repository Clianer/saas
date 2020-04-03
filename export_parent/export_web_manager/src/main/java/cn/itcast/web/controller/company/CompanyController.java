package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/7 18:33
 * @Version 1.0
 **/
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Reference
    private CompanyService companyService;

    /**
     * 查看所有企业
     *
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(defaultValue = "1")int pageNum,
                             @RequestParam(defaultValue = "5") int pageSize) {
        PageInfo<Company> pageInfo = companyService.findByPage(pageNum, pageSize);
        ModelAndView mv = new ModelAndView();
        //跳转到页面
        mv.setViewName("company/company-list");
        //把List放到请求域
        mv.addObject("pageInfo", pageInfo);
        return mv;
    }

    //进入修改页面
    @RequestMapping("toAdd")
    public String toAdd() {
        return "company/company-add";
    }

    //添加||修改操作
    @RequestMapping("edit")
    public String edit(Company company) {
        if (StringUtils.isEmpty(company.getId())) {
            companyService.add(company);
        } else {
            companyService.upData(company);
        }
        return "redirect:/company/list";
    }

    //进入更新页面
    @RequestMapping("toUpdate")
    public ModelAndView toUpdate(String id) {
        Company company = companyService.findById(id);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/company/company-update");
        mv.addObject("company", company);
        return mv;
    }

    //删除一家企业
    @RequestMapping("delete")
    public String delete(String id) {
        companyService.del(id);
        return "redirect:/company/list";
    }
    //批量删除企业
    @RequestMapping("delMany")
    public String delMany(String[] ids) {
        if (ids != null && ids.length > 0){
            for (String id : ids) {
                companyService.del(id);
            }
        }
            return "redirect:/company/list";
    }
}
