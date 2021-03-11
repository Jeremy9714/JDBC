package test;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 获取Connection对象
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
        info.setProperty("user","root");
        info.setProperty("password","9714");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二: 对方式一的迭代
    @Test
    public void testConnection2() throws Exception {
        //通过反射，获取Driver实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/test";

        //提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("name","root");
        info.setProperty("password","9714");

        //建立连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }
}
