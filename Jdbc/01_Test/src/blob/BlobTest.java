package blob;

import bean.Customer;
import org.junit.Test;
import util.JDBCUtils;

import java.io.*;
import java.sql.*;

/**
 * 测试使用PreparedStatement操作Blob类型的数据
 *
 * @author Chenyang
 * @create 2021-03-12-20:44
 */
public class BlobTest {

    //查询Blob类型的字段
    @Test
    public void testQuery() {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id=?";
            ps = connect.prepareStatement(sql);
            ps.setInt(1, 24);
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date date = rs.getDate("birth");
                Customer customer = new Customer(id, name, email, date);
                System.out.println(customer);

                //将Blob类型的字段下载并保存到本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("imag/output.jpg");
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(connect, ps, rs);
        }
    }

    //向Customer表中插入Blob类型的字段
    @Test
    public void testInsert() {
        Connection connect = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try {
            connect = JDBCUtils.getConnection();
            String sql = "insert into customers(name, email, birth, photo) values(?,?,?,?)";
            ps = connect.prepareStatement(sql);
            ps.setObject(1, "芒亨");
            ps.setObject(2, "mh@sina.com");
            ps.setObject(3, "1997-07-14");

            //插入Blob类型的字段
            fis = new FileInputStream(new File("imag/MHRise.jpg"));
            ps.setBlob(4, fis);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(connect, ps);
        }
    }
}
