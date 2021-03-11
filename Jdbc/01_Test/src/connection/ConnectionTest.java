package connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 获取Connection对象
 *
 * @author Chenyang
 * @create 2021-03-11-16:36
 */
public class ConnectionTest {

    //方式一
    @Test
    public void testConnection1() throws SQLException {
        //mySql的驱动
        Driver driver = new com.mysql.jdbc.Driver();

        //JDBC url用于标识一个被注册的驱动程序
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();

        //将连接需要的用户名和密码封装在properties里
        info.setProperty("user", "root");
        info.setProperty("password", "9714");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二: 对方式一的迭代: 不出现第三方的api
    @Test
    public void testConnection2() throws Exception {
        //通过反射，获取Driver实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "9714");

        //建立连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三: 使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {
        //获取驱动
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //注册驱动
        DriverManager.registerDriver(driver);

        //提供要连接的数据库和连接所需用户名和密码
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "9714");

        //获取连接
        Connection connect = DriverManager.getConnection(url, info);
        System.out.println(connect);
    }

    //方式四: 只需要加载驱动，不需要显示的注册驱动
    @Test
    public void testConnection4() throws Exception {
        //提供要连接的数据库和连接所需用户名和密码
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "9714");

        //加载Driver: 在mySql的Driver实现类中定义了静态代码块，会自动调用驱动管理器对象注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //获取连接
        Connection connect = DriverManager.getConnection(url, info);
        System.out.println(connect);
    }

    //方式五: 将数据库连接需要的基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    @Test
    public void testConnection5() throws Exception {
        //加载配置文件
        InputStream resource = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        //创建Properties对象读取基本信息
        Properties prop = new Properties();
        prop.load(resource);
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        String url = prop.getProperty("url");
        String driverClass = prop.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connect = DriverManager.getConnection(url, user, password);
        System.out.println(connect);
    }
}
