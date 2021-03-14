package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
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

    //数据库连接池只需要提供一个
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    //创建一个DBCP数据库连接池
    private static DataSource dbcp;
    static {
        try {
            Properties prop = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            prop.load(is);
            dbcp = BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建一个Druid数据库连接池
    private static DataSource druid;
    static {
        try {
            Properties prop = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            prop.load(is);
            druid = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过druid数据库连接池获取数据库连接
    public static Connection getConnectionViaDruid() throws Exception {
        Connection connect = druid.getConnection();
        return connect;
    }

    //通过c3p0数据库连接池获取数据库连接
    public static Connection getConnectionViaDBPS() throws Exception {
        Connection connect = dbcp.getConnection();
        return connect;
    }

    //通过c3p0数据库连接池获取数据库连接
    public static Connection getConnectionViaC3P0() throws Exception {
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

    //使用工具类DBUtils，实现资源的关闭
    public static void closeResourceViaDBUtil(Connection connect, Statement ps, ResultSet rs){
        //close()方法在方法内部判断参数对象是否为空
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(connect);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        //closeQuietly方法在方法内处理异常
        DbUtils.closeQuietly(rs);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(connect);

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
