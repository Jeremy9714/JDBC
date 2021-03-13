package transaction;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Chenyang
 * @create 2021-03-13-10:57
 */
public class TransactionTest {

    //------------未考虑数据库事务的情况下的操作-------------
    @Test
    public void testUpdate() {
        String sql = "update user_table set balance = balance - 100 where user = ? ";
        update(sql, "AA");

        //模拟网络异常
        System.out.println(10 / 0);

        String sql1 = "update user_table set balance = balance + 100 where user = ?";
        update(sql1, "BB");
        System.out.println("转账成功");
    }

    //通用的增、删、改操作---Version 1.0
    public int update(String sql, Object... args) {
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);

            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            //影响了几条数据
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps);
        }
        return 0;
    }

    //------------考虑数据库事务后的操作-------------
    @Test
    public void testUpdateWithTx(){
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();

            //1.取消数据的自动提交
            connect.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user = ?";
            update(connect, sql, "AA");

            //模拟事务执行中出现的错误
            //System.out.println(10/0);

            String sql1 = "update user_table set balance = balance + 100 where user = ?";
            update(connect, sql1, "BB");
            System.out.println("转账完成");

            //2.数据提交
            connect.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //3.回滚数据
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            try {
                //恢复为自动提交数据
                //主要针对于数据库连接池的使用
                connect.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(connect, null);
        }
    }

    //通用的增、删、改操作---Version 2.0(考虑上事务)
    public int update(Connection connect, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }
}
