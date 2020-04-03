import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author han
 * @Date 2020/3/11 17:06
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class TestUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    RoleService roleService;


    @Test
    public void TestParent() {
        userService.delete("0d39fa39-5e4d-47ac-ae01-acedd82904bb");
    }

    @Test
    public void TestUpdata(){
        User user = new User();
        user.setUserName("lisi");
        userService.update(user);
    }

    @Test
    public void TestModule() {
        System.out.println(moduleService.findById("2"));
    }

    @Test
    public void TestRole() {
        System.out.println(roleService.findById("4028a1cd4ee2d9d6014ee2df4c6a0000"));
    }
}
