package preparedstatement;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 针对于customers表的查询操作
 *
 * @author Chenyang
 * @create 2021-03-12-10:44
 */
public class CustomerForQuery {

    @Test
    public void test() {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "select * from customers where id > ?";
            ps = connect.prepareStatement(sql);
            ps.setInt(1, 8);

            //执行操作并返回结果集
            resultSet = ps.executeQuery();

            //处理结果集
            while (resultSet.next()) {// next()方法判断结果集的下一条是否有数据，返回值为boolean类型

                //获取当前数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //将数据封装成一个对象
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源(连接、Statement对象和结果集)
            JDBCUtils.closeResource(connect, ps, resultSet);
        }
    }
}
