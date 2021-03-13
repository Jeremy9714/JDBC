package preparedstatement;

import bean.Customer;
import bean.Order;
import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现针对不同表的通用查询操作
 *
 * @author Chenyang
 * @create 2021-03-12-17:16
 */
public class PreparedStatementQuery {

    //查询表中的多条记录
    @Test
    public void getList() {
        String sql1 = "select id,name,email from customers where id > ?";
        List<Customer> customers = getList(Customer.class, sql1, 1);
        customers.forEach(System.out::println);
        System.out.println("-------------------------------------------");

        String sql2 = "select order_id orderId, order_name orderName, order_date orderDate from `order`";
        List<Order> orders = getList(Order.class, sql2);
        orders.forEach(System.out::println);
    }

    //查询表中的一条记录
    @Test
    public void testGetInstance() {
        String sql = "select id,name,email from customers where id =?";
        Customer cust = getInstance(Customer.class, sql, 1);
        System.out.println(cust);

        String sql2 = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_name = ?";
        Order order = getInstance(Order.class, sql2, "AA");
        System.out.println(order);
    }

    //针对不同表的通用查询操作，返回表中的多条记录
    public <T> List<T> getList(Class<T> clazz, String sql, Object... args) {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            //创建集合对象
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                T obj = clazz.newInstance();
                for (int i = 0; i < count; ++i) {
                    Object value = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps, rs);
        }
        return null;
    }

    //针对不同表的通用查询操作，返回表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            if (rs.next()) {
                T obj = clazz.newInstance();
                for (int i = 0; i < count; ++i) {
                    Object value = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps, rs);
        }
        return null;
    }
}
