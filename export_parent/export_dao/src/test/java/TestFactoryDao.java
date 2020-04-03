import cn.itcast.dao.cargo.FactoryDao;
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
public class TestFactoryDao {
    @Autowired
    private FactoryDao factoryDao;

    @Test
    public void findAll(){
        System.out.println(factoryDao.selectByPrimaryKey("1"));
    }
}
