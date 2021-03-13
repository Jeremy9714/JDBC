package transaction;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;

/**
 * 连接测试
 * @author Chenyang
 * @create 2021-03-13-10:52
 */
public class ConnectionTest {

    @Test
    public void testConnection() throws Exception {
        Connection connect = JDBCUtils.getConnection();
        System.out.println(connect);
    }
}
