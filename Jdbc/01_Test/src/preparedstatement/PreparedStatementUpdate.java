package preparedstatement;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * PreparedStatement的增删改操作
 *
 * @author Chenyang
 * @create 2021-03-11-20:57
 */
public class PreparedStatementUpdate {

    //向表中添加一条记录
    @Test
    public void testInsert() {
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            //读取4个基本信息
            InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties prop = new Properties();
            prop.load(resource);

            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            String driverClass = prop.getProperty("driverClass");
            String url = prop.getProperty("url");

            //加载驱动
            Class.forName(driverClass);

            //获取连接
            connect = DriverManager.getConnection(url, user, password);

            //预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name, email, birth) values(?,?,?)"; //?是占位符
            ps = connect.prepareStatement(sql);

            //填充占位符
            ps.setString(1, "猛汉");
            ps.setString(2, "menghan@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(sdf.parse("2021-03-26").getTime());
            ps.setDate(3, date);

            //执行操作
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
