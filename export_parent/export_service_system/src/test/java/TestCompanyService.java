import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author han
 * @Date 2020/3/7 19:30
 * @Version 1.0
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class TestCompanyService {


/*
   */
/* @Reference
    CompanyService companyService;
    @Test
    public void findall(){
        System.out.println(companyService.findAll());
    }

    /**
     * pagehelper最终执行的sql：
     * SELECT count(0) FROM ss_company
     * select * from ss_company LIMIT ?, ?
     *//*

    @Test
    public void findByPage(){
        int pageNum = 2;
        int pageSize = 1;

        PageInfo<Company> pageInfo =
                companyService.findByPage(pageNum, pageSize);

        System.out.println("总页数：" + pageInfo.getPages());//1
        System.out.println("总记录数：" + pageInfo.getTotal());//2
        System.out.println("当前页数据：" + pageInfo.getList());
        System.out.println("当前页：" + pageInfo.getPageNum());//1
        System.out.println("页大小：" + pageInfo.getPageSize());//2

    }
*/

}
