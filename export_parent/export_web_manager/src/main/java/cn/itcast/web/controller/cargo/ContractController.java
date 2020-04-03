package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    // 注入dubbo的服务接口代理对象
    @Reference
    private ContractService contractService;

    /**
     * 列表查询
     */
    @RequestMapping("/list")
    public String findByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        // 构造条件对象
        ContractExample example = new ContractExample();
        // 根据创建时间降序查询
        example.setOrderByClause("create_time desc");
        // 条件构造器
        ContractExample.Criteria criteria = example.createCriteria();
        // 添加查询条件
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //权限控制
        User loginUser = getLoginUser();
        if (loginUser.getDegree() == 4) {
            criteria.andCreateByEqualTo(loginUser.getUserId());
        } else if (loginUser.getDegree() == 3) {
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        } else if (loginUser.getDegree() == 2) {
            PageInfo<Contract> pageInfo = contractService.findByDeptId(loginUser.getDeptId(), pageNum, pageSize);
            session.setAttribute("pageInfo", pageInfo);
            return "cargo/contract/contract-list";
        }
        // 查询
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example, pageNum, pageSize);
        // 保存
        request.setAttribute("pageInfo", pageInfo);
        return "cargo/contract/contract-list";
    }

    /**
     * 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 添加 / 修改
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(contract.getId())) {
            // 记录创建时间
            contract.setCreateTime(new Date());
            contract.setCreateBy(getLoginUser().getUserId());
            contract.setCreateDept(getLoginUser().getDeptId());

            // id 如果为空，说明是添加
            contractService.save(contract);
        } else {
            // 修改
            contractService.update(contract);
        }
        // 添加成功，重定向到列表
        return "redirect:/cargo/contract/list";
    }

    /**
     * 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        // 根据id查询
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);

        return "cargo/contract/contract-update";
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        // 删除
        contractService.delete(id);
        // 添加成功，重定向到列表
        return "redirect:/cargo/contract/list";
    }

    /**
     * 购销合同（1）查看
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        //根据ID查询
        Contract contract = contractService.findById(id);
        session.setAttribute("contract", contract);
        return "/cargo/contract/contract-view";
    }

    /**
     * 提交合同
     */
    @RequestMapping("submit")
    public String submit(String id) {
        //构造Contract对象
        Contract contract = new Contract();
        //设置更新ID
        contract.setId(id);
        //直接设置状态
        contract.setState(1);
        //根据ID更新一条数据
        contractService.update(contract);
        return "redirect:/cargo/contract/list";
    }

    /**
     * 取消合同
     */
    @RequestMapping("cancel")
    public String cancel(String id) {
        //构造Contract对象
        Contract contract = new Contract();
        //设置更新ID
        contract.setId(id);
        //直接设置状态
        contract.setState(0);
        //根据ID更新一条数据
        contractService.update(contract);
        return "redirect:/cargo/contract/list";
    }
}
