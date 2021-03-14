package connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.impl.C3P0PooledConnection;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * C3P0数据库连接池
 * @author Chenyang
 * @create 2021-03-14-9:54
 */
public class C3P0Test {

    //方式一
    @Test
    public void testConnection() throws Exception {
        //创建c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();

        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("9714");

        //通过设置相关的参数，对连接池进行管理
        //设置数据库连接池初始化时的连接数
        cpds.setInitialPoolSize(10);

        Connection connect = cpds.getConnection();
        System.out.println(connect);

        //销毁整个数据库连接池
        //DataSources.destroy(cpds);
    }

    //方式二: 通过配置文件
    @Test
    public void testConnction2() throws Exception {
        //根据配置文件初始化c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection connect = cpds.getConnection();
        System.out.println(connect);

        //DataSources.destroy(cpds);
    }
    
}
