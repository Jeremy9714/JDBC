package dao2;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author Chenyang
 * @create 2021-03-13-18:10
 */
public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO {
    @Override
    public void insert(Connection connect, Customer customer) {
        String sql = "insert into Customers(name,email,birth) values(?,?,?)";
        int count = update(connect, sql, customer.getName(), customer.getEmail(), customer.getBirth());
        if (count != 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    @Override
    public void deleteById(Connection connect, int id) {
        String sql = "delete from customers where id = ?";
        int count = update(connect, sql, id);
        if (count != 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("此id不存在");
        }
    }

    @Override
    public void update(Connection connect, Customer customer) {
        String sql = "update customers set name = ?, email = ?, birth = ? where id =?";
        int count = update(connect, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());
        if (count != 0) {
            System.out.println("更新成功");
        } else {
            System.out.println("此用户不存在");
        }
    }

    @Override
    public Customer getCustomerById(Connection connect, int id) {
        String sql = "select id, name, email, birth from customers where id =?";
        Customer customer = getInstance(connect, sql, id);
        if (customer == null) {
            throw new RuntimeException("此id不存在");
        }
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connect) {
        String sql = "select id, name, email, birth from customers";
        List<Customer> list = getList(connect, sql);
        if (list.size() == 0) {
            throw new RuntimeException("此表为空");
        }
        return list;
    }

    @Override
    public long getCount(Connection connect) {
        String sql = "select count(*) from customers";
        return getValue(connect, sql);
    }

    @Override
    public Date getMaxBirth(Connection connect) {
        String sql = "select max(birth) from customers";
        return getValue(connect, sql);
    }

}
