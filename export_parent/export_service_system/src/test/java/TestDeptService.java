import cn.itcast.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author han
 * @Date 2020/3/10 2:38
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class TestDeptService {

    @Autowired
    private DeptService deptService;


    @Test
    public void TestDept(){
        int pageNum = 1;
        int pageSize = 5;
        String companyId = "1";
        System.out.println(deptService.findByPage(companyId,pageNum, pageSize));
    }

    @Test
    public void TestDel() {
        deptService.del("8a7e862458b9ed5b0158b9edc0e80000");
    }

    @Test
    public void TestParent() {
        deptService.del("4028827c4fb6202a014fb6209c730000");
    }
}
