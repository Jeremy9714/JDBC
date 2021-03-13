package dao2;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了针对于数据表的通用操作
 *
 * @author Chenyang
 * @create 2021-03-13-17:35
 */
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    {
        //获取当前BaseDAO子类继承的父类中的泛型
        //子类实例化时，会执行父类的代码块，这里的this指的是实例化的类型的对象
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType ParamType = (ParameterizedType) genericSuperclass;

        //获取了父类的泛型参数
        Type[] types = ParamType.getActualTypeArguments();
        clazz = (Class<T>) types[0];//泛型的第一个参数
    }

    //通用的增、删、改操作---Version 2.0(考虑上事务)
    public int update(Connection connect, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    //通用的查询操作，返回表中的一条记录---Version 2.0(考虑上事务)
    public T getInstance(Connection connect, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    //通用的查询操作，返回表中的多条记录---Version 2.0(考虑上事务)
    public List<T> getList(Connection connect, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();

            //创建集合对象
            List<T> list = new ArrayList<>();
            while (rs.next()) {
                T obj = clazz.newInstance();
                for (int i = 0; i < count; ++i) {
                    Object value = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(obj, value);
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    //用于查询特殊值的通用方法
    public <E> E getValue(Connection connect, String sql, Object... args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connect.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
