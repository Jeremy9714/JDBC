package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC工具类
 *
 * @author Chenyang
 * @create 2021-03-11-21:41
 */
public class JDBCUtils {

    //获取数据库连接
    public static Connection getConnection() throws Exception {
        //读取4个基本信息
        InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties prop = new Properties();
        prop.load(resource);

        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        String driverClass = prop.getProperty("driverClass");
        String url = prop.getProperty("url");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connect = DriverManager.getConnection(url, user, password);
        return connect;
    }

    //关闭数据库连接
    public static void closeResource(Connection connect, Statement ps) {
        //资源关闭
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (connect != null)
                connect.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
