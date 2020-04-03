package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @Author han
 * @Date 2020/3/10 2:41
 * @Version 1.0
 **/
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("list")
    public String findAll(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "5") int pageSize,
                           Model model
    ) {
        //调用数据库
        PageInfo<Dept> pageInfo = deptService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        model.addAttribute("pageInfo",pageInfo);
        return "/system/dept/dept-list";
    }

    /**
     * 进入添加页面
     * @param model
     * @return
     */
   @RequestMapping("toAdd")
   public String toAdd(Model model){
        //企业ID，暂时模拟，因为还没有登录
        String companyId = getLoginCompanyId();
        //查询所有的部门信息
        List<Dept> deptList = deptService.findAll(companyId);
        //放到请求域中
        model.addAttribute("deptList",deptList);
        return "/system/dept/dept-add";
    }

    /**
     * 保存添加信息
     * @param dept
     * @return
     */
    @RequestMapping("edit")
    public String edit(Dept dept){
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());
        if(StringUtils.isEmpty(dept.getId())){
            deptService.add(dept);
        }else {
            deptService.updata(dept);
        }
        return "redirect:/system/dept/list";
    }

    //进入修改页面
    @RequestMapping("toUpdate")
    public String toUpdate(String id,Model model){
        //根据ID查询该部门信息
        Dept dept = deptService.findById(id);
        //查询企业的所有部门但不包括当前部门
        List<Dept> deptList = deptService.findAllDept(getLoginCompanyId(),id);
        //把查询结果放到请求域中
        model.addAttribute("dept",dept);
        model.addAttribute("deptList",deptList);
        //返回页面
        return "/system/dept/dept-update";
    }
    //删除一个部门
    @RequestMapping("delete")
    @ResponseBody
    public HashMap<String, Integer> delete(String id){
        HashMap<String, Integer> result = new HashMap<>();
        boolean flag = deptService.del(id);
        if (flag){
            result.put("msg",1);
        }else {
            result.put("msg",0);
        }
        return result;
    }

/*    //批量删除部门
    @RequestMapping("delMany")
    private String delMany(String[] ids) {
        deptService.delMany(ids);
        //异步请求，返回信息给前段
        return "ok";
    }*/
}
