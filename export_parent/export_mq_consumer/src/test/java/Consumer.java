import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author han
 * @Date 2020/3/27 21:39
 * @Version 1.0
 **/
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq-consumer.xml");
        System.in.read();
    }
}
