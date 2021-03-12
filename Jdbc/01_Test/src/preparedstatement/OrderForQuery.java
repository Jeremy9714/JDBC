package preparedstatement;

import bean.Order;
import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 针对于Order表的通用查询操作
 * @author Chenyang
 * @create 2021-03-12-14:07
 */
public class OrderForQuery {

    @Test
    public void test(){
        //若表的字段名与类的属性名不相同，则必须在声明sql时，使用类的属性名来命名各字段的别名
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = orderForQuery(sql, 1);
        System.out.println(order);
    }

    //通用的查询操作
    public Order orderForQuery(String sql, Object ...args){
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            if(rs.next()){
                Order order = new Order();
                for(int i=0;i<count;++i){
                    Object value = rs.getObject(i + 1);

                    //获取列的别名: getColumnLabel(int)
//                  String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = order.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,value);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect,ps,rs);
        }
        return null;
    }
}
