package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductResult;
import cn.itcast.vo.ExportResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    // 注入报运单信息: 报运单dao、商品dao、商品附件dao
    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtEproductDao extEproductDao;

    // 注入购销合同相关信息: 购销合同dao、货物dao、附件dao
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<Export> findByPage(ExportExample exportExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Export> list = exportDao.selectByExample(exportExample);
        return new PageInfo<>(list);
    }

    @Override
    public void updateExport(ExportResult exportResult) {
        //1. 更新报运单 （状态、备注）
        Export export = new Export();
        export.setId(exportResult.getExportId());
        export.setMarks(exportResult.getRemark());
        export.setState(exportResult.getState());
        exportDao.updateByPrimaryKeySelective(export);
        //2.更新商品
        Set<ExportProductResult> products = exportResult.getProducts();
        if (products != null && products.size() > 0) {
            for (ExportProductResult product : products) {
                ExportProduct exportProduct = new ExportProduct();
                exportProduct.setId(product.getExportProductId());
                exportProduct.setTax(product.getTax());
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //0.主键ID
        export.setId(UUID.randomUUID().toString());
        //1、获取购销合同id
        String contractIds = export.getContractIds();
        //2、修改购销合同状态为2（已生成报运）、获取合同号并设置到报运单中
        //保运单号
        String contractNo = "";
        //分割多个报运单之间的ID
        String[] array = contractIds.split(",");
        //遍历数组
        for (String contractId : array) {
            //根据拆分的ID查询购销合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            //设置购销合同状态为2
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
            //获得合同号并通过空格拼接
            contractNo += contract.getContractNo() + " ";
        }
        export.setCustomerContract(contractNo);
        //3、根据购销合同id查询货物
        //构造查询条件
        ContractProductExample cpExample = new ContractProductExample();
        //查询多个ID
        cpExample.createCriteria().andContractIdIn(Arrays.asList(array));
        List<ContractProduct> cpList = contractProductDao.selectByExample(cpExample);
        // 定义一个map，保存货物id、商品id。 为了后面保存附件时候获取商品id用到。
        // key: 货物id；  value： 商品id
        Map<String, String> map = new HashMap<>();
        if (cpList != null && cpList.size() > 0) {
            //4、给报运单添加商品
            // 遍历货物
            for (ContractProduct contractProduct : cpList) {
                // A.创建商品对象
                ExportProduct exportProduct = new ExportProduct();
                // B.封装商品对象
                // B1. 对象拷贝（属性一致拷贝，不一致就不拷贝不报错。org.springframework.beans）
                BeanUtils.copyProperties(contractProduct, exportProduct);
                // B2. 设置商品id
                exportProduct.setId(UUID.randomUUID().toString());
                // B3. 设置商品关联的报运单id 【最关键】
                exportProduct.setExportId(export.getId());
                // C.保存商品
                exportProductDao.insertSelective(exportProduct);
                //保存货物ID和对应的商品ID
                map.put(contractProduct.getId(), exportProduct.getId());
            }
        }
        //5、根据购销合同id查询附件
        ExtCproductExample exExample = new ExtCproductExample();
        exExample.createCriteria().andContractIdIn(Arrays.asList(array));
        List<ExtCproduct> exList = extCproductDao.selectByExample(exExample);
        //6、给报运单的商品添加附件
        if (exList != null && exList.size() > 0) {
            for (ExtCproduct extCproduct : exList) {
                //创建商品附件对象
                ExtEproduct extEproduct = new ExtEproduct();
                //封装对象
                BeanUtils.copyProperties(extCproduct, extEproduct);
                //设置附件ID
                extEproduct.setId(UUID.randomUUID().toString());
                //设置附件关联商品的报运单ID
                extEproduct.setExportId(export.getId());
                //获得货物ID
                String contractProductId = extCproduct.getContractProductId();
                //设置商品id,从Map集合中取出
                extEproduct.setExportProductId(map.get(contractProductId));

            }
        }
        //7、设置报运单参数 (主键id、状态、商品数量、附件数量)
        export.setState(0);
        export.setProNum(cpList.size());
        export.setExtNum(exList.size());
        //8、生成报运单
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        //更新报运单
        exportDao.updateByPrimaryKeySelective(export);
        //更新报运单的商品
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }
}