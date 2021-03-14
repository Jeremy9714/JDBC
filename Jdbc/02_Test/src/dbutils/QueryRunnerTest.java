package dbutils;

import bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * JDBC工具类DBUtils的使用
 *
 * @author Chenyang
 * @create 2021-03-14-15:12
 */
public class QueryRunnerTest {

    //插入一条数据
    @Test
    public void testInsert() {
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();
            String sql = "insert into customers(name, email, birth) values(?,?,?)";
            //执行增删改
            int count = runner.update(connect, sql, "cxk", "cxk666@126.com", "1990-09-09");
            System.out.println("添加了" + count + "条记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    //BeanHandler: 时ResultSetHandler接口的实现类，用于封装表中的一条记录
    @Test
    public void testQuery() {
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();
            String sql = "select id, name, email, birth from customers where id = ?";

            //创建结果集处理器
            //BeanHandler是ResultSetHandler接口的实现类
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            //可以将表中的查询记录封装为指定类对象
            Customer customer = runner.query(connect, sql, handler, 27);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    //BeanListHandler: 时ResultSetHandler接口的实现类，用于封装表中多条记录构成的集合
    @Test
    public void testQueryForList() {
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();
            String sql = "select id, name, email, birth from customers where id < ?";
            //创建结果集处理器
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            //可以将表中的查询记录封装为指定类对象
            List<Customer> customers = runner.query(connect, sql, handler, 27);
            customers.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    //MapHandler: 时ResultSetHandler接口的实现类，对应表中的一条记录
    @Test
    public void testQueryForMap() {
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();
            String sql = "select id, name, email, birth from customers where id = ?";
            //创建结果集处理器
            MapHandler handler = new MapHandler();
            //将字段及对应字段值作为map中的key和value
            Map<String, Object> map = runner.query(connect, sql, handler, 27);
            for (Map.Entry entry : map.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    //MapListHandler: 时ResultSetHandler接口的实现类，对应表中的多条记录
    @Test
    public void testQueryForMapList() {
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();
            String sql = "select id, name, email, birth from customers where id < ?";
            //创建结果集处理器
            MapHandler handler = new MapHandler();
            //将字段及对应字段值作为map中的key和value，并将这些map添加到集合中
            MapListHandler mapList = new MapListHandler();
            List<Map<String, Object>> list = runner.query(connect, sql, mapList, 27);
            list.stream().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }

    //查询表中特殊值
    @Test
    public void testQueryValue(){
        Connection connect = null;
        try {
            QueryRunner runner = new QueryRunner();
            connect = JDBCUtils.getConnectionViaDruid();

            //String sql = "select count(*) from customers";
            String sql = "select max(birth) from customers";

            ScalarHandler handler = new ScalarHandler();

            //Long value = (Long) runner.query(connect, sql, handler);
            Date maxBirth = (Date) runner.query(connect, sql, handler);
            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, null);
        }
    }
}
