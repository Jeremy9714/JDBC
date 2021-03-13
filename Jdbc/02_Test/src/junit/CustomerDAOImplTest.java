package junit;

import bean.Customer;
import dao.CustomerDAOImpl;
import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CustomerDAOImpl类的单元测试
 *
 * @author Chenyang
 * @create 2021-03-13-19:08
 */
public class CustomerDAOImplTest {

    private CustomerDAOImpl dao = new CustomerDAOImpl();

    @Test
    public void insert() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            dao.insert(connect, new Customer(1, "良三", "liangsan@qq.com", new Date(100000L)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void deleteById() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            dao.deleteById(connect, 22);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void update() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            dao.update(connect, new Customer(23, "笔头", "bitou@gmail.com", new Date(29393L)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void getCustomerById() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(connect, 24);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void getAll() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(connect);
            System.out.println("表中数据一览");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void getCount() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            long count = dao.getCount(connect);
            System.out.println("表中数据的条目数: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection connect = null;
        try {
            connect = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(connect);
            System.out.println("最晚生日为: " + maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }
}