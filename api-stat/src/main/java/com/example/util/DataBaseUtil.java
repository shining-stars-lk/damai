package com.example.util;


import com.example.model.ColumnInfo;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DataBaseUtil {
    private static Logger log = Logger.getLogger(DataBaseUtil.class.toString());

    private static Map<String, ColumnInfo> tableInfoMap = new ConcurrentHashMap<>();
    private static Map<String, List<ColumnInfo>> sqlColumns = new ConcurrentHashMap<>();

    public static DataSource getDataSource() {
        return Context.getDataSource();
    }

    public static int insert(String sql, Object[] values) {
        try {
            Connection connection = getDataSource().getConnection();
            return insert(connection, sql, values);
        } catch (SQLException e) {
            if (e.getClass().getSimpleName().equals("CommunicationsException") ||e.getClass().getSimpleName().equals("MySQLTimeoutException") ) {
                log.severe("Failure database connection!");
            }else {
                log.severe("Error insert:"+sql);
            }
        }
        return 0;
    }

    public static int insert(Connection connection, String sql, Object[] values) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            int n = statement.executeUpdate();
            return n;
        } catch (SQLIntegrityConstraintViolationException e) {

        } catch (SQLException e) {
            if (e.getClass().getSimpleName().equals("CommunicationsException") ||e.getClass().getSimpleName().equals("MySQLTimeoutException") ) {
                log.severe("Failure database connection!");
            }else {
                log.severe("Error insert:"+sql);
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static int truncateByTable(Connection connection, String table) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("truncate table " + table);
            int n = statement.executeUpdate();
            return n;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static int update(String sql, Object[] values) {
        try (Connection connection = getDataSource().getConnection()) {
            return update(connection, sql, values);
        } catch (SQLException e) {
            if (e.getClass().getSimpleName().equals("CommunicationsException") ||e.getClass().getSimpleName().equals("MySQLTimeoutException") ) {
                log.severe("Failure database connection!");
            }else {
                log.severe("Error update:"+sql);
            }
        }
        return 0;
    }

    public static int update(Connection connection, String sql, Object[] values) {
        return insert(connection, sql, values);
    }


    public static List<Map<String, Object>> query(Connection connection, String sql, Object[] values) {
        List<Map<String, Object>> list = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            statement.setQueryTimeout(15);
            resultSet = statement.executeQuery();
            List<ColumnInfo> columns = null;
            if (sqlColumns.containsKey(sql)) {
                columns = sqlColumns.get(sql);
            }else {
                final ResultSetMetaData metaData = statement.getMetaData();
                columns = getColumns(metaData);
                sqlColumns.put(sql,columns);
            }
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (ColumnInfo column : columns) {
                    map.put(column.getName(), getColumnValue(resultSet, column));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            log.severe("Error query for database");
            if (e.getClass().getSimpleName().equals("CommunicationsException") ||e.getClass().getSimpleName().equals("MySQLTimeoutException") ) {
                connection = null;
                log.severe("Failure database connection!");
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return list;
    }

    public static boolean existsById(Connection connection, String sql, Object id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement = setParams(statement, id);
            statement.setQueryTimeout(15);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connection = null;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    public static <T> List<T> query(Connection connection, String sql, Object[] values, Class<T> c) {
        List<T> list = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            statement.setQueryTimeout(15);
            resultSet = statement.executeQuery();
            List<ColumnInfo> columns = null;
            if (sqlColumns.containsKey(sql)) {
                columns = sqlColumns.get(sql);
            }else {
                final ResultSetMetaData metaData = statement.getMetaData();
                resultSet = statement.executeQuery();
                columns = getColumns(metaData);
                sqlColumns.put(sql,columns);
            }
            Field[] fields = null;
            while (resultSet.next()) {
                T object = c.newInstance();
                if (null == fields) {
                    fields = object.getClass().getDeclaredFields();
                }
                for (Field field : fields) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    ColumnInfo column = null;
                    if (tableInfoMap.containsKey(columns.get(0).getTable() + field.getName())) {
                        column = tableInfoMap.get(columns.get(0).getTable() + field.getName());
                    } else {
                        column = matchColumn(field, columns);
                        if (null == column) {
                            continue;
                        }
                        tableInfoMap.put(column.getTable() + field.getName(), column);
                    }
                    field.setAccessible(true);
                    Object columnValue = getColumnValue(resultSet, column);
                    if (field.getName().equals("methodType")) {
                        field.set(object, MethodType.valueOf(columnValue + ""));
                    } else {
                        field.set(object, columnValue);
                    }
                    field.setAccessible(false);
                }
                list.add(object);
            }
        }catch (SQLException e) {
            log.severe("Error query for database!");
            if (e.getClass().getSimpleName().equals("CommunicationsException") ||e.getClass().getSimpleName().equals("MySQLTimeoutException") ) {
                connection = null;
                log.severe("Failure database connection!");
            }
        }catch (IllegalAccessException e) {
            log.severe("Error access for database!");
        } catch (InstantiationException e) {
            log.severe("Error instance for database!");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }
        return list;
    }

    public static <T> List<T> query(String sql, Object[] values, Class<T> c) {
        List<T> list = null;
        try (Connection connection = getDataSource().getConnection()) {
            list = query(connection, sql, values, c);
        } catch (SQLException throwables) {
            list = new ArrayList<>();
        }
        return list;
    }

    public static ColumnInfo matchColumn(Field field, List<ColumnInfo> columns) {
        for (ColumnInfo column : columns) {
            String columnReplace = column.getName().replace("_", "");
            if (field.getName().equalsIgnoreCase(columnReplace)) {
                return column;
            }
        }
        return null;
    }

    private static PreparedStatement setParams(PreparedStatement statement, Object[] values) {
        try {
            int length = values.length;
            for (int i = 0; i < length; i++) {
                if (values[i] == null) {
                    statement.setObject(i + 1, null);
                } else if (values[i] instanceof String) {
                    statement.setString(i + 1, (String) values[i]);
                } else if (values[i] instanceof Double) {
                    statement.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) values[i]);
                } else {
                    throw new DataBaseException("Invalid type=" + values[i].getClass().getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    private static PreparedStatement setParams(PreparedStatement statement, Object value) {
        try {
            if (value == null) {
                statement.setObject(1, null);
            } else if (value instanceof String) {
                statement.setString(1, (String) value);
            } else if (value instanceof Double) {
                statement.setDouble(1, (Double) value);
            } else if (value instanceof Integer) {
                statement.setInt(1, (Integer) value);
            } else if (value instanceof Boolean) {
                statement.setBoolean(1, (Boolean) value);
            } else {
                throw new DataBaseException("Invalid type=" + value.getClass().getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    private static Object getColumnValue(ResultSet resultSet, ColumnInfo column) throws SQLException {
        if ("VARCHAR".equals(column.getDataType()) || "TEXT".equals(column.getDataType())) {
            return resultSet.getString(column.getName());
        } else if ("DECIMAL".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getBigDecimal(column.getName()).doubleValue();
        } else if ("DOUBLE".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getDouble(column.getName());
        } else if ("INT".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getInt(column.getName());
        } else if ("DATETIME".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getTimestamp(column.getName());
        } else {
            return resultSet.getObject(column.getName());
        }
    }

    private static List<ColumnInfo> getColumns(ResultSetMetaData metaData) throws SQLException {
        List<ColumnInfo> colnames = new ArrayList<ColumnInfo>();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String table = metaData.getTableName(i + 1);
            String colname = metaData.getColumnName(i + 1);
            String colType = metaData.getColumnTypeName(i + 1);
            ColumnInfo tableInfo = new ColumnInfo();
            tableInfo.setTable(table);
            tableInfo.setName(colname);
            tableInfo.setDataType(colType);
            colnames.add(tableInfo);
        }
        return colnames;
    }

}
