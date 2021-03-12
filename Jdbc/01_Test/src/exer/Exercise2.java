package exer;

import bean.Student;
import org.junit.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 练习二:
 *
 * @author Chenyang
 * @create 2021-03-12-18:34
 */
public class Exercise2 {

    //删除数据
    @Test
    public void testDelete(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入学生的准考证号");
        String examCard = scanner.nextLine();

        String sql="delete from examstudent where ExamCard = ?";
        int result = update(sql, examCard);
        if(result!=0){
            System.out.println("删除成功");
        }else{
            System.out.println("此学生不存在，请重新输入");
        }

    }

    //查询结果
    @Test
    public void testRead() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("身份证号还是准考证号");
        System.out.println("a.身份证号");
        System.out.println("b.准考证号");
        String input = scanner.nextLine();
        String sql = "";
        if ("a".equalsIgnoreCase(input)) {
            sql = "select FlowID flowID, Type type, IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where IDCard = ?";
            System.out.println("请输入身份证号:");
            String idCard = scanner.next();
            Student student = getInstance(Student.class, sql, idCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("该学生不存在，请重新进入程序");
            }
        } else if ("b".equalsIgnoreCase(input)) {
            sql = "select FlowID flowID, Type type, IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where ExamCard = ?";
            System.out.println("请输入准考证号:");
            String examCard = scanner.next();
            Student student = getInstance(Student.class, sql, examCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("该学生不存在，请重新进入程序");
            }
        } else {
            System.out.println("无效的输入，请重新进入程序");
        }
    }

    //针对不同表的通用查询操作，返回表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connect = JDBCUtils.getConnection();
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

    //添加操作
    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("四级/六级:");
        String type = scanner.next();
        System.out.println("身份证号:");
        String idCard = scanner.next();
        System.out.println("准考证号:");
        String examId = scanner.next();
        System.out.println("学生姓名:");
        String studentName = scanner.next();
        System.out.println("区域:");
        String location = scanner.next();
        System.out.println("成绩:");
        int grade = scanner.nextInt();

        String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";
        int count = update(sql, type, idCard, examId, studentName, location, grade);
        if (count > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    //通用的增删改操作
    public int update(String sql, Object... args) {
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
