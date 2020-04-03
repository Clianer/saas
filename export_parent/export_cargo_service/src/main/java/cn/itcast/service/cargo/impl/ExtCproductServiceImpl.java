package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproductExample);
        PageInfo<ExtCproduct> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExtCproduct extCproduct) {
        //设置ID
        extCproduct.setId(UUID.randomUUID().toString());
        //计算金额
        Double amount = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null) {
            //单价*数量
            amount = extCproduct.getPrice() * extCproduct.getCnumber();
        }
        extCproduct.setAmount(amount);
        //保存货物
        extCproductDao.insertSelective(extCproduct);
        //修改购销合同
        //根据附件所属的合同ID查询
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //设置附件数量（原本+1）
        contract.setExtNum(contract.getExtNum() + 1);
        //设置总金额（总金额+附件金额）
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //更新到合同中
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        //获得修改之前的金额
        ExtCproduct oldExt = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        //计算金额
        Double amount = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null) {
            //单价*数量
            amount = extCproduct.getPrice() * extCproduct.getCnumber();
        }
        extCproduct.setAmount(amount);
        //保存货物
        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        //修改购销合同
        //根据附件所属的购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(oldExt.getContractId());
        //设置总金额（总金额+修改后-修改前）
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldExt.getAmount());
        //更新到合同中
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //根据ID查询要删除的对象
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //查询合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //修改合同总金额和附件数量
        contract.setExtNum(contract.getExtNum() - 1);
        contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount());
        //删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }
}