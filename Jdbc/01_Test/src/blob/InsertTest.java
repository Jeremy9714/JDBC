package blob;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 批量操作
 *
 * @author Chenyang
 * @create 2021-03-13-9:42
 */
public class InsertTest {

    //批量插入的方式一: 使用PreparedStatement
    @Test
    public void testInsert1(){
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = connect.prepareStatement(sql);
            long start = System.currentTimeMillis();

            for (int i = 0; i < 20000; ++i) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: " + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps);
        }
    }

    //批量插入的方式二: addBatch()、executeBatch()、clearBatch()
    //mysql服务器默认关闭批量处理，需要在配置文件的url后面加上参数
    //?rewriteBatchedStatements=true
    @Test
    public void testInsert2(){
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = connect.prepareStatement(sql);
            long start = System.currentTimeMillis();

            for (int i = 1; i <= 1000000; ++i) {
                ps.setObject(1, "name_" + i);

                //积攒sql
                ps.addBatch();
                if(i%500==0){
                    //运行batch
                    ps.executeBatch();

                    //清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: " + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps);
        }
    }

    //批量插入方式三: 设置连接不允许自动提交数据
    @Test
    public void testInsert3(){
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            connect = JDBCUtils.getConnection();

            //关闭自动提交
            connect.setAutoCommit(false);

            String sql = "insert into goods(name) values(?)";
            ps = connect.prepareStatement(sql);
            long start = System.currentTimeMillis();

            for (int i = 1; i <= 1000000; ++i) {
                ps.setObject(1, "name_" + i);

                //积攒sql
                ps.addBatch();
                if(i%500==0){
                    //运行batch
                    ps.executeBatch();

                    //清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            connect.commit();

            long end = System.currentTimeMillis();
            System.out.println("花费的时间为: " + (end - start)); //9757
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps);
        }
    }
}
