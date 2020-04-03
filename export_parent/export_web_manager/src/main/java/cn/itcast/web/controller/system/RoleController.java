package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author han
 * @Date 2020/3/11 16:30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ModuleService moduleService;

    //用户列表分页
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int PageSize) {
        //1.调用service查询用户列表
        PageInfo<Role> pageInfo =
                roleService.findByPage(getLoginCompanyId(), pageNum, PageSize);
        //2.将用户列表保存到request域中
        request.setAttribute("pageInfo",pageInfo);
        //3.跳转到对象的页面
        return "system/role/role-list";
    }

    /**
     * 进入新增用户页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        String companyId = getLoginCompanyId();
        //1.查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);
        //2.存入request域
        request.setAttribute("deptList",deptList);
        return "system/role/role-add";
    }

    /**
     * 新增或更新用户
     */
    @RequestMapping("/edit")
    public String edit(Role role) {
        String company = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        role.setCompanyId(company);
        role.setCompanyName(companyName);
        //1.判断是否具有id属性
        if(StringUtils.isEmpty(role.getId())){
            //2.用户id为空，执行保存
            roleService.save(role);
        }else{
            //3.用户id不为空，执行更新
            roleService.update(role);
        }
        return "redirect:/system/role/list";
    }

    /**
     * 进入到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id, Model model){
        Role role = roleService.findById(id);
        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);
        model.addAttribute("role",role);
        model.addAttribute("deptList",deptList);
        return "system/role/role-update";
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Integer> delete(String id){
        Map<String,Integer> result = new HashMap<>();
        boolean flag = roleService.delete(id);
        if (flag){
            result.put("msg",1);
        } else {
            result.put("msg",2);
        }
        //跳转到修改界面
        return result;
    }

    /**
     * 进入角色权限分配页面
     */
    @RequestMapping("roleModule")
    public String roleModule(String id){
        //查询一条数据
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    //权限（树）
    @RequestMapping("getZtreeNodes")
    @ResponseBody
    public List<Map<String, Object>> getZtreeNodes(String id){
        //查询所有权限
        List<Module> modules = moduleService.findAll();
        //查询一个角色所拥有的所有权限
        List<Module> moduleByRoleId = moduleService.findModuleByRoleId(id);
        //构造一个List集合用于存放所有权限信息
        List<Map<String,Object>> list = new ArrayList<>();
        //遍历所有权限找出角色所拥有的权限
        for (Module module : modules) {
            //初始化Map集合
            Map<String,Object> map = new HashMap<>();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            if(moduleByRoleId.contains(module)){
                //{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true}
                map.put("checked",true);
            }
            map.put("open",true);
            list.add(map);
        }
        return list;
    }
    //修改权限
    @RequestMapping("updateRoleModule")
    public String updateRoleModule(String id , String moduleIds){
        roleService.updateRoleModule(id,moduleIds);
        return "redirect:/system/role/list.do";
    }
}
