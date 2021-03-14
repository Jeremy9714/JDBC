package connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Druid数据库连接池
 * @author Chenyang
 * @create 2021-03-14-11:45
 */
public class DruidTest {

    @Test
    public void testConnection() throws Exception {
        Properties prop = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        prop.load(is);
        DataSource druid = DruidDataSourceFactory.createDataSource(prop);
        Connection connect = druid.getConnection();
        System.out.println(connect);
    }
}
