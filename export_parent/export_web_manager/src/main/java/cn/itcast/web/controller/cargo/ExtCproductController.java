package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.util.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    // 注入dubbo的服务接口代理对象
    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 货物列表和添加
     * 功能入口：购销合同列表，点击“货物”，进入货物列表与添加页面
     * 请求地址：http://localhost:8080/cargo/extCproduct/list.do?
     *          contractId=9f62535d-fcc0-4761-bf30-54314689797f
     *          contractProductId=71ea2941-57a5-4152-a9e6-d18f501f2fc9
     * 响应地址：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String findByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            String contractId,String contractProductId) {
        //查询生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        session.setAttribute("factoryList",factoryList);


        //通过货物ID查询所有的附件
        ExtCproductExample example = new ExtCproductExample();
        example.createCriteria().andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> pageInfo = extCproductService.findByPage(example, pageNum, pageSize);
        session.setAttribute("pageInfo",pageInfo);

        //保存购销合同ID
        session.setAttribute("contractId",contractId);
        //保存货物ID
        session.setAttribute("contractProductId",contractProductId);
        return "cargo/extc/extc-list";
    }

    /**
     * 添加 / 修改
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) {
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(extCproduct.getId())) {
            // id 如果为空，说明是添加
            extCproductService.save(extCproduct);
        } else {
            // 修改
            extCproductService.update(extCproduct);
        }
        // 添加成功，重定向到列表
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+
                "&contractProductId="+extCproduct.getContractProductId();
    }

    /**
     * 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId) {
        // 根据id查询
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct", extCproduct);

        //查询生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        session.setAttribute("factoryList", factoryList);

        session.setAttribute("contractId",contractId);
        session.setAttribute("contractProductId",contractProductId);

        return "cargo/extc/extc-update";
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id, String contractId,String contractProductId) {
        // 删除
        extCproductService.delete(id);
        // 添加成功，重定向到列表
        return "redirect:/cargo/extCproduct/list.do?contractId="
                +contractId+"&contractProductId="+contractProductId;
    }
}
