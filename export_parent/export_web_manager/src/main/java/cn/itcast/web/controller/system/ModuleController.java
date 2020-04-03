package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/11 16:30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleService moduleService;

    //用户列表分页
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int PageSize) {
        //1.调用service查询用户列表
        PageInfo<Module> pageInfo =
                moduleService.findByPage(pageNum, PageSize);
        //2.将用户列表保存到request域中
        request.setAttribute("pageInfo",pageInfo);
        //3.跳转到对象的页面
        return "system/module/module-list";
    }

    /**
     * 进入新增用户页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        String companyId = getLoginCompanyId();
        //1.查询所有部门
        List<Module> moduleList = moduleService.findAll();
        //2.存入request域
        request.setAttribute("menus",moduleList);
        return "system/module/module-add";
    }

    /**
     * 新增或更新用户
     */
    @RequestMapping("/edit")
    public String edit(Module module) {
        //1.判断是否具有id属性
        if(StringUtils.isEmpty(module.getId())){
            //2.用户id为空，执行保存
            moduleService.save(module);
        }else{
            //3.用户id不为空，执行更新
            moduleService.update(module);
        }
        return "redirect:/system/module/list";
    }

    /**
     * 进入到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //查询所有信息，为了构造下拉列表
        List<Module> list = moduleService.findAll();
        //查询一条数据，用于数据回显
        Module module = moduleService.findById(id);
        request.setAttribute("menus",list);
        request.setAttribute("module",module);
        return "system/module/module-update";
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    public String  delete(String id){
        moduleService.delete(id);
        return "redirect:/system/module/list";
    }
}
