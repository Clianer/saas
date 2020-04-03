package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author han
 * @Date 2020/3/21 12:05
 * @Version 1.0
 **/
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 合同管理 列表： 只显示购销合同状态为1的记录。
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "5") int pageSize) {
        //构造查询条件
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria().andCompanyIdEqualTo(getLoginCompanyId());
        criteria.andStateEqualTo(1);
        //查询根据ID数据库
        PageInfo<Contract> pageInfo = contractService.findByPage(example, pageNum, pageSize);
        session.setAttribute("pageInfo", pageInfo);
        return "cargo/export/export-contractList";
    }

    /**
     * 出口报运单列表
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize) {
        //构造查询条件
        ExportExample example = new ExportExample();
        //根据公司ID查询该公司下所有要出口保运单的列表
        example.createCriteria().andCompanyIdEqualTo(getLoginCompanyId());
        PageInfo<Export> pageInfo = exportService.findByPage(example, pageNum, pageSize);
        session.setAttribute("pageInfo", pageInfo);
        return "cargo/export/export-list";
    }

    /**
     * 进入报运页面
     */
    @RequestMapping("/toExport")
    public String toExport(String id) {
        session.setAttribute("id", id);
        return "cargo/export/export-toExport";
    }

    /**
     * 添加 / 修改
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(export.getId())) {
            // id 如果为空，说明是添加
            exportService.save(export);
        } else {
            // 修改
            exportService.update(export);
        }
        // 添加成功，重定向到列表
        return "redirect:/cargo/export/list";
    }

    /**
     * 进入更新页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //查询报运单
        Export export = exportService.findById(id);
        session.setAttribute("export", export);
        //查询报运单下所有的商品
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> eps = exportProductService.finAll(example);
        session.setAttribute("eps", eps);
        return "cargo/export/export-update";
    }

    /**
     * 提交
     *
     * @param id
     * @return
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        Export export = new Export();
        export.setId(id);
        export.setState(1);
        exportService.update(export);
        return "redirect:/cargo/export/list";
    }

    /**
     * 取消
     *
     * @param id
     * @return
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Export export = new Export();
        export.setId(id);
        export.setState(0);
        exportService.update(export);
        return "redirect:/cargo/export/list";
    }

    /**
     * 电子报运
     */
    @RequestMapping("/exportE")
    public String exportE(String id) {
        // A.创建websevice请求请求数据对象
        ExportVo exportVo = new ExportVo();
        //根据ID查询报运单
        Export export = exportService.findById(id);
        //复制到对象中
        BeanUtils.copyProperties(export, exportVo);
        //设置报运单ID
        exportVo.setExportId(id);
        //封装报运单的商品
        List<ExportProductVo> productVoList = exportVo.getProducts();
        //查询报运单的商品
        //构造查询条件
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.finAll(example);
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                ExportProductVo exportProductVo = new ExportProductVo();
                BeanUtils.copyProperties(exportProduct, exportProductVo);
                //设置报运单ID
                exportProductVo.setExportId(id);
                //设置商品ID
                exportProductVo.setExportProductId(exportProduct.getId());
                //放到集合中
                productVoList.add(exportProductVo);
            }
        }
        //B.电子报运(1)远程访问海关报运平台，录入报运单信息
        WebClient.create("http://localhost:8083/ws/export/user").post(exportVo);
        // C.电子报运(2)远程访问海关报运平台,查询报运结果
        ExportResult exportResult =
                WebClient.create("http://localhost:8083/ws/export/user/" + id).get(ExportResult.class);
        // D.根据报运结果，更新后台数据库
        exportService.updateExport(exportResult);
        // E.返回页面
        return "redirect:/cargo/export/list";
    }
}
