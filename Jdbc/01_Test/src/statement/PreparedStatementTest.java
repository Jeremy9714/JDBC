package statement;

import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @author Chenyang
 * @create 2021-03-12-17:42
 */
public class PreparedStatementTest {

    //使用PreparedStatement解决sql注入问题
    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名:");
        String username = scanner.nextLine();
        System.out.print("请输入密码:");
        String password = scanner.nextLine();


//      SELECT user, password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1'='1'
        String sql = "SELECT user, password FROM user_table WHERE user = ? AND password = ?";
        User user = getInstance(User.class, sql, username, password);
        if (user != null) {
            System.out.println("登陆成功");
            System.out.println(user);
        } else {
            System.out.println("用户名不存在或密码不正确");
        }
    }

    //针对不同表的通用查询操作，返回表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
            //预编译
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
