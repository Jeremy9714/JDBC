package exer;

import org.junit.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * 练习一
 *
 * @author Chenyang
 * @create 2021-03-12-18:02
 */
public class Exercise1 {

    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名:");
        String name = scanner.nextLine();
        System.out.print("请输入邮箱:");
        String email = scanner.nextLine();
        System.out.println("请输入生日:");
        String birthday = scanner.nextLine();

        String sql = "insert into customers(name, email, birth) values(?,?,?)";
        int result = update(sql, name, email, birthday);
        if(result!=0){
            System.out.println("操作成功");
        }else{
            System.out.println("操作失败");
        }

    }

    public int update(String sql, Object... args){
        Connection connect = null;
        PreparedStatement ps = null;
        try {
            connect = JDBCUtils.getConnection();
            ps = connect.prepareStatement(sql);

            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            //ps.execute();
            //执行操作，并返回执行增删改操作影响的行数，0代表没有任何影响
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connect, ps);
        }
        return 0;
    }
}
