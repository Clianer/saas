package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(
            ContractProductExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractExample);
        PageInfo<ContractProduct> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }
    @Override
    public void save(ContractProduct contractProduct) {
        contractProduct.setId(UUID.randomUUID().toString());
        //A. 计算货物金额
        Double amount = 0d;
        // 设置货物金额
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        contractProduct.setAmount(amount);
        //B. 修改购销合同
        //B1.根据购销合同id查询
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //B2.修改总金额 = 总金额 + 货物金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //B3.修改货物数量
        contract.setProNum(contract.getProNum() + 1);
        //B4. 修改
        contractDao.updateByPrimaryKeySelective(contract);
        //C. 保存货物
        contractProductDao.insertSelective(contractProduct);
    }
    @Override
    public void update(ContractProduct contractProduct) {
        //A. 计算货物金额
        Double amount = 0d;
        // 设置货物金额
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        contractProduct.setAmount(amount);
        // 根据货物id查询数据库中的货物对象，获取货物金额
        ContractProduct cp = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        // 修改前的货物金额
        Double oldAmount = cp.getAmount();
        //B. 修改购销合同
        //B1.根据购销合同id查询
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //B2.修改总金额 = 总金额 + 修改后 - 修改前
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount);
        //B3.修改购销合同
        contractDao.updateByPrimaryKeySelective(contract);
        //C. 修改货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
    }
    @Override
    public void delete(String id) {
        Double extAmount = 0d;
        //A.查询货物金额
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        //根据货物ID进行查询，循环删除附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(contractProduct.getId());
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(example);
        for (ExtCproduct extCproduct : extCproducts) {
            //累加附件所有的金额
            extAmount += extCproduct.getAmount();
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        //B. 修改购销合同总金额、货物数量
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //B1. 总金额 = 总金额 - 货物金额 - 附件金额
        contract.setTotalAmount(contract.getTotalAmount() - contractProduct.getAmount() - extAmount);
        //B3.修改购销合同
        contract.setProNum(contract.getProNum() - 1);
        //附件数量
        contract.setExtNum(contract.getExtNum() - extCproducts.size());
        //更新合同货物信息
        contractDao.updateByPrimaryKeySelective(contract);
        contractProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ContractProductVo> findByShipTime(String companyID, String inputDate) {
        return contractProductDao.findByShipTime(companyID,inputDate);
    }
}