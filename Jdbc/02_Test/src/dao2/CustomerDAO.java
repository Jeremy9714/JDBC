package dao2;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范针对于Customer表的常用操作
 * @author Chenyang
 * @create 2021-03-13-18:01
 */
public interface CustomerDAO {

    //插入一条数据
    void insert(Connection connect, Customer customer);

    //删除指定id对应的数据
    void deleteById(Connection connect, int id);

    //更新一条数据
    void update(Connection connect, Customer customer);

    //获取指定id对应的数据
    Customer getCustomerById(Connection connect, int id);

    //获取全部数据
    List<Customer> getAll(Connection connect);

    //获取表中数据的条目数
    long getCount(Connection connect);

    //获取最晚的出生日期
    Date getMaxBirth(Connection connect);
}
