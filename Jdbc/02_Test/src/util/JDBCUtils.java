package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC工具类
 *
 * @author Chenyang
 * @create 2021-03-11-21:41
 */
public class JDBCUtils {

    //将数据库连接池声明为静态，减少资源的占用
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    //通过c3p0数据库连接池获取数据库连接
    public static Connection getConnection1() throws Exception {
        Connection connect = cpds.getConnection();
        return connect;
    }

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

    //关闭数据库连接和Statement
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

    //关闭资源的操作
    public static void closeResource(Connection connect, Statement ps, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
