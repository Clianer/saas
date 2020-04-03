import cn.itcast.dao.system.DeptDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author han
 * @Date 2020/3/10 2:31
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-dao.xml")
public class TestDept {
    @Autowired
    private DeptDao deptDao;

    @Test
    public void findAll(){
        System.out.println(deptDao.findById("100"));
    }

    @Test
    public void del() {
        System.out.println(deptDao.findParentById("100"));
    }
}
