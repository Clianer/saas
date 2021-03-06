import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author han
 * @Date 2020/3/15 20:27
 * @Version 1.0
 **/
public class CompanyProvider {
    public static void main(String[] args) throws IOException {
        //1.加载配置文件
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        //2.启动
        ac.start();
        //3.输入后停止
        System.in.read();
    }
}
