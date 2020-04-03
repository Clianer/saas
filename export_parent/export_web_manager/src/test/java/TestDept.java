import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author han
 * @Date 2020/3/12 20:02
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class TestDept {
    @Autowired
    private DeptService deptService;
    @Test
    public void testDept(){
        PageInfo<Dept> byPage = deptService.findByPage("1", 1, 5);
        System.out.println(byPage);
    }

    @Test
    public void testMd5(){
        String s = "saas@export.com";
        Md5Hash md5Hash = new Md5Hash("1", s);
        System.out.println(md5Hash);
    }
}
