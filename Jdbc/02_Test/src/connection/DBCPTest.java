package connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBCP数据库连接池
 * @author Chenyang
 * @create 2021-03-14-10:59
 */
public class DBCPTest {

    //方式一
    @Test
    public void getConnection() throws Exception {
        //创建了DBCP数据库连接池
        BasicDataSource dbcp = new BasicDataSource();

        //设置数据库连接池基本信息
        dbcp.setDriverClassName("com.mysql.jdbc.Driver");
        dbcp.setUrl("jdbc:mysql:///test");
        dbcp.setUsername("root");
        dbcp.setPassword("9714");

        //设置关羽数据库连接池管理的相关属性
        dbcp.setInitialSize(10);
        dbcp.setMaxActive(10);
        dbcp.setMaxIdle(10);
        dbcp.setRemoveAbandonedTimeout(10);

        Connection connect = dbcp.getConnection();
        System.out.println(dbcp);

    }

    //方式二: 使用配置文件
    @Test
    public void getConnection1() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        Properties prop = new Properties();
        prop.load(is);
        //创建DBCP数据库连接池
        DataSource dbcp = BasicDataSourceFactory.createDataSource(prop);
        Connection connect = dbcp.getConnection();
        System.out.println(connect);
    }
}
