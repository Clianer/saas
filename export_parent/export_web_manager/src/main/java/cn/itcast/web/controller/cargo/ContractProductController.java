package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.util.FileUploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    // 注入dubbo的服务接口代理对象
    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 货物列表和添加
     * 功能入口：购销合同列表，点击“货物”，进入货物列表与添加页面
     * 请求地址：http://localhost:8080/cargo/contractProduct/list.do
     * 请求参数：contractId=8  【购销合同id】
     * 响应地址：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String findByPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            String contractId) {
        //得到货物对象
        ContractProductExample example = new ContractProductExample();
        //构造条件
        ContractProductExample.Criteria criteria = example.createCriteria();
        //按照ID进行查询
        criteria.andContractIdEqualTo(contractId);
        //查询
        PageInfo<ContractProduct> pageInfo = contractProductService.findByPage(example, pageNum, pageSize);
        //放入到会话域
        session.setAttribute("pageInfo", pageInfo);

        //得到货物对象
        FactoryExample factoryExample = new FactoryExample();
        //构造条件
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        //按照ID进行查询
        factoryExampleCriteria.andCtypeEqualTo("货物");
        //查询工厂用于遍历工厂
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //存放到会话域
        session.setAttribute("factoryList", factoryList);

        //保存ID到会话域
        session.setAttribute("contractId", contractId);
        return "cargo/product/product-list";
    }

    /**
     * 添加 / 修改
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) {
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(contractProduct.getId())) {
            try {
                String url = "http://" + fileUploadUtil.upload(productPhoto);
                contractProduct.setProductImage(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // id 如果为空，说明是添加
            contractProductService.save(contractProduct);
        } else {
            // 修改
            contractProductService.update(contractProduct);
        }
        // 添加成功，重定向到列表
        return "redirect:/cargo/contractProduct/list?contractId=" + contractProduct.getContractId();
    }

    /**
     * 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        // 根据id查询
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);

        //查询生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        session.setAttribute("factoryList", factoryList);

        return "cargo/product/product-update";
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id, String contractId) {
        // 删除
        contractProductService.delete(id);
        // 添加成功，重定向到列表
        return "redirect:/cargo/contractProduct/list?contractId=" + contractId;
    }

    /**
     * 进入上传货物页面
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        session.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    /**
     * 上传货物
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {
        //1. 根据上传的文件创建工作簿
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        //2. 获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //3. 获取总行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //获得企业信息
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        //4. 遍历所有信息：第一行是标题，所以从第二行开始遍历
        for (int i = 1; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            ContractProduct contractProduct = new ContractProduct();
            //设置表中每一个值
            contractProduct.setFactoryName(row.getCell(1).getStringCellValue());
            contractProduct.setProductNo(row.getCell(2).getStringCellValue());
            contractProduct.setCnumber((int) row.getCell(3).getNumericCellValue());
            contractProduct.setPackingUnit(row.getCell(4).getStringCellValue());
            contractProduct.setLoadingRate(row.getCell(5).getNumericCellValue() + "");
            contractProduct.setBoxNum((int) row.getCell(6).getNumericCellValue());
            contractProduct.setPrice(row.getCell(7).getNumericCellValue());
            contractProduct.setProductDesc(row.getCell(8).getStringCellValue());
            contractProduct.setProductRequest(row.getCell(9).getStringCellValue());
            //设置购销合同ID
            contractProduct.setContractId(contractId);
            //设置企业信息
            contractProduct.setCompanyId(companyId);
            contractProduct.setCompanyName(companyName);
            //根据工厂名称查询获得工厂ID
            String factoryId = factoryService.findFactoryNameById(contractProduct.getFactoryName());
            //设置工厂ID
            contractProduct.setFactoryId(factoryId);
            //保存货物
            contractProductService.save(contractProduct);
        }
        return "redirect:/cargo/contract/list";
    }
}
