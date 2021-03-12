package preparedstatement;

import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;

/**
 * 针对于customers表的查询操作
 *
 * @author Chenyang
 * @create 2021-03-12-10:44
 */
public class CustomerForQuery {

    @Test
    public void testCommonRead(){
        String sql = "select id, name, email, birth from customers where id > ?";
        read(sql,10);
    }

    //Customer表的通用查询操作
    public void read(String sql, Object ...args){
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);
            for(int i=0;i<args.length;++i){
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过结果集元数据获取结果集中的列数
            int count = rsmd.getColumnCount();

            while(rs.next()){
                Customer customer = new Customer();
                for (int i =0; i< count;++i){
                    //获取列值
                    Object value = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //通过反射给Customer对象指定的属性赋值
                    Field field = customer.getClass().getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer,value);
                }
                //输出结果
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect,ps,rs);
        }
    }

    @Test
    public void testRead() {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth from customers where id > ?";
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
