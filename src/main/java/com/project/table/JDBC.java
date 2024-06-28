/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.connection.MysqlConnection;
import com.project.connection.OracleConnection;
import com.project.connection.PostgresqlConnection;
import com.project.connection.SqliteConnection;

/**
 *
 * @author ITU
 */
public class JDBC {

    private String table;
    private Class<?> classes;
    private List<Field> fields;
    private List<String> columns;
    private List<Method> allMethod;
    private List<Method> getters;
    private List<Method> setters;
    private Connection connection;

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public JDBC() throws SQLException, ClassNotFoundException {
        setClasses(this.getClass());
        if (!this.classes.isAnnotationPresent(TableName.class)) {
            throw new RuntimeException("TableName annotation missing",
                    new Throwable("You forget to include TableName annotation"));
        }
        setAllMethod();
        setFields();
        setColumns();
        setSetters();
        setGetters();
        setConnection();
        setTable();
    }

    /**
     *
     */
    private void setAllMethod() {
        this.allMethod = Arrays.asList(this.classes.getDeclaredMethods()).stream()
                .filter(prdct -> prdct.isAnnotationPresent(Getter.class) || prdct.isAnnotationPresent(Setter.class))
                .collect(Collectors.toList());
    }

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void setConnection() throws SQLException, ClassNotFoundException {
        String database = this.classes.getAnnotation(TableName.class).database();
        String user = this.classes.getAnnotation(TableName.class).user();
        String password = this.classes.getAnnotation(TableName.class).password();
        String driver = this.classes.getAnnotation(TableName.class).driver();
        switch (driver.toLowerCase()) {
            case "oracle":
                this.connection = OracleConnection.getConnection(user, password);
                // System.out.println("oracle");
                break;
            case "postgres":
                this.connection = PostgresqlConnection.getConnection(database, user, password);
                // System.out.println("postgres");
                break;
            case "mysql":
                this.connection = MysqlConnection.getConnection(database, user, password);
                // System.out.println("mysql");
                break;
            case "sqlite":
                this.connection = SqliteConnection.getConnection(database);
                // System.out.println("sqlite");
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param setters
     */
    private void setSetters(List<Method> setters) {
        this.setters = setters;
    }

    /**
     *
     * @param getters
     */
    private void setGetters(List<Method> getters) {
        this.getters = getters;
    }

    /**
     *
     * @param field
     * @return
     */
    private Method getGetter(String field) {
        return getters.stream()
                .filter(prdct -> prdct.getAnnotation(Getter.class).columnName().toLowerCase()
                        .equals(field.toLowerCase()))
                .findFirst().get();
    }

    /**
     *
     * @param field
     * @return
     */
    private Method getSetter(String field) {
        return setters.stream()
                .filter(prdct -> prdct.getAnnotation(Setter.class).columnName().toLowerCase()
                        .equals(field.toLowerCase()))
                .findFirst().get();
    }

    /**
     *
     */
    private void setGetters() {
        List<Method> gets = this.allMethod;
        gets = gets.stream().filter(set -> set.isAnnotationPresent(Getter.class)).collect(Collectors.toList());
        setGetters(gets);
    }

    /**
     *
     */
    private void setSetters() {
        List<Method> sets = this.allMethod;
        sets = sets.stream().filter(set -> set.isAnnotationPresent(Setter.class)).collect(Collectors.toList());
        setSetters(sets);
    }

    /**
     *
     * @param table
     */
    private void setTable(String table) {
        this.table = table;
    }

    /**
     *
     */
    private void setTable() {
        setTable(this.classes.getAnnotation(TableName.class).name());
    }

    /**
     *
     */
    private void setFields() {
        List<Field> attributs = Arrays.asList(this.classes.getDeclaredFields());
        attributs = attributs.stream().filter(field -> field.isAnnotationPresent(ColumnField.class))
                .collect(Collectors.toList());
        setFields(attributs);
    }

    /**
     *
     * @param fields
     */
    private void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     *
     * @return
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     *
     * @param field
     * @return
     */
    private String getColumn(Field field) {
        return field.getAnnotation(ColumnField.class).columnName();
    }

    /**
     *
     * @param columns
     */
    private void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     *
     */
    private void setColumns() {
        List<String> colonnes = fields.stream()
                .filter(condition -> condition.isAnnotationPresent(ColumnField.class))
                .map(value -> value.getAnnotation(ColumnField.class).columnName())
                .collect(Collectors.toList());
        this.setColumns(colonnes);
    }

    public String getPrimaryKey() {
        List<Field> attributs = this.fields;
        Field primaryKey = attributs.stream()
                .filter(condition -> condition.isAnnotationPresent(ColumnField.class))
                .filter(field -> field.getAnnotation(ColumnField.class).primaryKey())
                .findFirst().get();
        return primaryKey.getAnnotation(ColumnField.class).columnName();
    }

    /**
     *
     * @param classes
     */
    private void setClasses(Class<?> classes) {
        this.classes = classes;
    }

    /**
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    private String getSequence(Connection connection) throws SQLException {
        String aRetourner;
        try (Statement statement = connection.createStatement()) {
            aRetourner = "";
            String req;
            if (connection.toString().trim().contains("oracle")) {
                req = "SELECT " + this.table + "_seq.nextval FROM dual";
            } else {
                req = "SELECT nextval('" + this.table + "_seq')";
            }
            try (ResultSet resultSet = statement.executeQuery(req)) {
                while (resultSet.next()) {
                    aRetourner = String.valueOf(resultSet.getInt("NEXTVAL")).trim();
                    break;
                }
            }
        }
        return aRetourner;
    }

    /**
     *
     * @param field
     * @return
     */
    private boolean isFieldPrimaryKey(Field field) {
        return field.getAnnotation(ColumnField.class).primaryKey();
    }

    /**
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    private String createRightSequence(Connection connection) throws SQLException {
        String seq = getSequence(connection);
        int len = seq.length();
        String nombre = "";
        for (int i = 0; i < 6 - len; i++) {
            nombre += "0";
        }
        nombre += seq;
        return this.table + nombre;
    }

    /**
     *
     * @param connection
     * @return
     * @throws Exception
     */
    private int save(Connection connection) throws Exception {
        int aRetourner;
        try (Statement statement = connection.createStatement()) {
            String data = "(";
            int count = 0;
            boolean entrer;
            for (Field field : this.fields) {
                entrer = true;
                if (isFieldPrimaryKey(field)) {
                    if (field.getAnnotation(ColumnField.class).defaultValue() == false) {
                        data += "'" + createRightSequence(connection) + "'";
                    } else {
                        data += "DEFAULT";
                    }
                } else {
                    Object method = getGetter(field.getAnnotation(ColumnField.class).columnName()).invoke(this);
                    if (field.getAnnotation(ColumnField.class).defaultValue() == true) {
                        data += "DEFAULT";
                        entrer = false;
                    }
                    if (method == null && entrer) {
                        data += "NULL";
                        entrer = false;
                    }
                    if (entrer) {
                        if (field.getType().getSimpleName().equals("int")
                                || field.getType().getSimpleName().equals("double")
                                || field.getType().getSimpleName().equals("float")) {
                            data += method;
                        } else if (connection.toString().trim().contains("oracle")
                                && field.getType().getSimpleName().equals("Date")) {
                            data += "TO_DATE('" + method + "', 'YYYY-MM-DD')";
                        } else {
                            data += "'" + method + "'";
                        }
                    }
                }
                if (count == fields.size() - 1) {
                    data += ")";
                } else {
                    data += ",";
                }

                count++;
            }
            String request = "INSERT INTO " + this.table + " VALUES" + data;
            aRetourner = statement.executeUpdate(request);
        }
        return aRetourner;
    }

    private String saveInPostgres(Connection connection) throws Exception {
        String aRetourner = null;
        try (Statement statement = connection.createStatement()) {
            String data = "(";
            int count = 0;
            boolean entrer;
            for (Field field : this.fields) {
                entrer = true;
                if (isFieldPrimaryKey(field)) {
                    if (field.getAnnotation(ColumnField.class).defaultValue() == false) {
                        data += "'" + createRightSequence(connection) + "'";
                    } else {
                        data += "DEFAULT";
                    }
                } else {
                    Object method = getGetter(field.getAnnotation(ColumnField.class).columnName()).invoke(this);
                    if (field.getAnnotation(ColumnField.class).defaultValue() == true) {
                        data += "DEFAULT";
                        entrer = false;
                    }
                    if (method == null && entrer) {
                        data += "NULL";
                        entrer = false;
                    }
                    if (entrer) {
                        if (field.getType().getSimpleName().equals("int")
                                || field.getType().getSimpleName().equals("double")
                                || field.getType().getSimpleName().equals("float")) {
                            data += method;
                        } else if (connection.toString().trim().contains("oracle")
                                && field.getType().getSimpleName().equals("Date")) {
                            data += "TO_DATE('" + method + "', 'YYYY-MM-DD')";
                        } else {
                            data += "'" + method + "'";
                        }
                    }
                }
                if (count == fields.size() - 1) {
                    data += ")";
                } else {
                    data += ",";
                }

                count++;
            }
            String request = "INSERT INTO " + this.table + " VALUES" + data + " RETURNING " + getPrimaryKey();
            ResultSet rSet = statement.executeQuery(request);
            if (rSet.next()) {
                aRetourner = rSet.getString(getPrimaryKey());
            }
        }
        return aRetourner;
    }

    public String insertAndReturnId(Connection connection) throws Exception {
        String aRetourner = null;
        try {
            aRetourner = saveInPostgres(connection);
            if (aRetourner != null) {
                System.out.println("New data added");
                // return true;
            } else {
                System.out.println("No data added");
                connection.rollback();
                // return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage(), e.getCause());
        }
        return aRetourner;
    }

    /**
     *
     * @return @throws Exception
     */
    public boolean insert() throws Exception {
        try {
            int s = save(connection);
            if (s > 0) {
                System.out.println("New data added");
                connection.commit();
                return true;
            } else {
                System.out.println("No data added");
                connection.rollback();
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @param connection
     * @return
     * @throws Exception
     */
    public boolean insert(Connection connection) throws Exception {
        try {
            int s = save(connection);
            if (s > 0) {
                System.out.println("New data added");
                return true;
            } else {
                System.out.println("No data added");
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /*
     * 
     * @param <T>
     * 
     * @param statement
     * 
     * @param req
     * 
     * @return
     * 
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked" })
    private <T> LinkedList<T> doRequest(Statement statement, String req) throws Exception {
        LinkedList<T> aRetourner = new LinkedList<>();
        try (ResultSet resultSet = statement.executeQuery(req)) {
            while (resultSet.next()) {
                Object o = this.getClass().cast(this.getClass().getConstructor().newInstance());
                setData(resultSet, o);
                aRetourner.add((T) o);
            }
        }
        return aRetourner;
    }

    /**
     *
     * @param <T>
     * @param connection
     * @return
     * @throws Exception
     */
    public <T> LinkedList<T> select(Connection connection) throws Exception {
        LinkedList<T> aRetourner;
        try (Statement statement = connection.createStatement()) {
            String req = "SELECT * FROM " + this.table;
            String r = addCondition(connection);
            req += r;
            aRetourner = doRequest(statement, req);
        }
        return aRetourner;
    }

    /**
     *
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> LinkedList<T> select() throws Exception {
        return select(connection);
    }

    /**
     *
     * @param <T>
     * @param requete
     * @return
     * @throws Exception
     */
    public <T> LinkedList<T> select(String requete) throws Exception {
        return select(connection, requete);
    }

    /**
     *
     * @param <T>
     * @param connection
     * @param requete
     * @return
     * @throws Exception
     */
    public <T> LinkedList<T> select(Connection connection, String requete) throws Exception {
        LinkedList<T> aRetourner;
        try (Statement statement = connection.createStatement()) {
            String req = "SELECT * FROM " + this.table;
            req += " " + requete;

            aRetourner = doRequest(statement, req);
        }
        return aRetourner;
    }

    /**
     *
     * @param <T>
     * @param connection
     * @param id
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T selectById(Connection connection, String id) throws Exception {
        String primaryKey = this.getPrimaryKey();
        String identifiant = (id.trim().matches("[+-]?\\d*(\\.\\d+)?")) ? id : "'" + id + "'";
        return (T) select("WHERE " + primaryKey + " = " + identifiant).getFirst();
    }

    /**
     *
     * @param <T>
     * @param id
     * @return
     * @throws Exception
     */
    public <T> T selectById(String id) throws Exception {
        return selectById(connection, id);
    }

    /**
     *
     * @param <T>
     * @param id
     * @return
     * @throws Exception
     */
    public <T> T selectById(int id) throws Exception {
        return selectById(connection, String.valueOf(id));
    }

    /**
     *
     * @param connection
     * @return
     * @throws Exception
     */
    private String addCondition(Connection connection) throws Exception {
        LinkedList<Field> fieldsNotNull = getFieldsNotNull();
        String req = "";
        if (fieldsNotNull != null) {
            req += " WHERE ";
            int i = 0;
            for (Field field : fieldsNotNull) {
                Object ob = getGetter(field.getAnnotation(ColumnField.class).columnName()).invoke(this);
                switch (field.getType().getSimpleName()) {
                    case "String":
                        if (connection.toString().toLowerCase().trim().contains("postgres")) {
                            req += getColumn(field) + " = '" + ob.toString() + "'";
                        } else {
                            req += getColumn(field) + " LIKE('%" + ob.toString() + "%')";
                        }
                        break;
                    case "Date":
                        req += getColumn(field) + " = '" + ob + "'";
                        break;
                    default:
                        req += field.getName() + " = " + ob;
                }
                if (i != fieldsNotNull.size() - 1) {
                    req += " AND ";
                }
                i++;
            }
        }
        return req;
    }

    /**
     *
     * @param <T>
     * @param connection
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private <T> int modify(Connection connection, String field, T value) throws Exception {
        int aRetourner;
        try (Statement statement = connection.createStatement()) {
            if (!value.toString().trim().matches("[+-]?\\d*(\\.\\d+)?")) {
                value = (T) ("'" + value + "'");
            }
            if (value.getClass().getSimpleName().equals("Date") && connection.toString().trim().contains("oracle")) {
                value = (T) ("TO_DATE('" + value + "', 'YYYY-MM-DD')");
            }
            String req = "UPDATE " + this.table + " SET " + field + " = " + value;
            String r = addCondition(connection);
            Object object = getGetter(getPrimaryKey()).invoke(this);
            req += " WHERE " + getPrimaryKey() + " ='" + object.toString() + "'";
            // System.out.println(req);
            aRetourner = statement.executeUpdate(req);
        }
        return aRetourner;
    }

    /**
     *
     * @param connection
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    public boolean update(Connection connection, String field, String value) throws Exception {
        try {
            int s = modify(connection, field, value);
            if (s > 0) {
                System.out.println("Data updated");
                return true;
            } else {
                System.out.println("Data not updated");
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    public boolean update(String field, String value) throws Exception {
        try {
            int s = modify(connection, field, value);
            if (s > 0) {
                connection.commit();
                System.out.println("Data updated");
                return true;
            } else {
                connection.rollback();
                System.out.println("Data not updated");
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @param connection
     * @return
     * @throws Exception
     */
    private int effacer(Connection connection) throws Exception {
        int aRetourner;
        try (Statement statement = connection.createStatement()) {
            String req = "DELETE FROM " + this.table;
            String r = addCondition(connection);
            req += r;
            aRetourner = statement.executeUpdate(req);
        }
        return aRetourner;
    }

    /**
     *
     * @param connection
     * @return
     * @throws Exception
     */
    public boolean delete(Connection connection) throws Exception {
        try {
            int s = effacer(connection);
            if (s > 0) {
                System.out.println("Data deleted");
                return true;
            } else {
                System.out.println("Data not deleted");
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @return @throws Exception
     */
    public boolean delete() throws Exception {
        try {
            int s = effacer(connection);
            if (s > 0) {
                connection.commit();
                System.out.println("Data deleted");
                return true;
            } else {
                connection.rollback();
                System.out.println("Data not deleted");
                return false;
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException e) {
            connection.rollback();
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    /**
     *
     * @return @throws Exception
     */
    private LinkedList<Field> getFieldsNotNull() throws Exception {
        LinkedList<Field> aRetourner = new LinkedList<>();
        for (Field field : fields) {
            Object ob = getGetter(field.getAnnotation(ColumnField.class).columnName()).invoke(this);
            if (ob != null) {
                if (field.getType().getSimpleName().equals("int")
                        || field.getType().getSimpleName().equals("double")
                        || field.getType().getSimpleName().equals("float")) {
                    if (Double.parseDouble(ob.toString().trim()) != 0.0) {
                        aRetourner.add(field);
                    }
                } else {
                    aRetourner.add(field);
                }
            }
        }
        if (aRetourner.isEmpty()) {
            return null;
        }
        return aRetourner;
    }

    /**
     *
     * @param resultSet
     * @param obj
     * @throws Exception
     */
    private void setData(ResultSet resultSet, Object obj) throws Exception {
        int i = 0;
        for (Field field : fields) {
            Method method = getSetter(field.getAnnotation(ColumnField.class).columnName());
            switch (field.getType().getSimpleName()) {
                case "int":
                    int entier = resultSet.getInt(this.columns.get(i));
                    method.invoke(obj, entier);
                    break;
                case "Integer":
                    int integer = resultSet.getInt(this.columns.get(i));
                    method.invoke(obj, integer);
                    break;
                case "double":
                    double doublee = resultSet.getDouble(this.columns.get(i));
                    method.invoke(obj, doublee);
                    break;
                case "Double":
                    Double doubl = resultSet.getDouble(this.columns.get(i));
                    method.invoke(obj, doubl);
                    break;
                case "String":
                    String string = resultSet.getString(this.columns.get(i));
                    method.invoke(obj, string);
                    break;
                case "Date":
                    Date date = resultSet.getDate(this.columns.get(i));
                    method.invoke(obj, date);
                    break;
                case "Time":
                    Time time = resultSet.getTime(this.columns.get(i));
                    method.invoke(obj, time);
                    break;
                case "float":
                    float tsingevana = resultSet.getFloat(this.columns.get(i));
                    method.invoke(obj, tsingevana);
                    break;
                case "Float":
                    Float ft = resultSet.getFloat(this.columns.get(i));
                    method.invoke(obj, ft);
                    break;
                case "Timestamp":
                    Timestamp timestamp = resultSet.getTimestamp(this.columns.get(i));
                    method.invoke(obj, timestamp);
                    break;
                case "Array":
                    Array array = resultSet.getArray(this.columns.get(i));
                    method.invoke(obj, array);
                    break;
                case "long":
                    long l = resultSet.getLong(this.columns.get(i));
                    method.invoke(obj, l);
                    break;
                case "Long":
                    Long lg = resultSet.getLong(this.columns.get(i));
                    method.invoke(obj, lg);
                    break;
                case "boolean":
                    boolean bl = resultSet.getBoolean(this.columns.get(i));
                    method.invoke(obj, bl);
                    break;
                case "Boolean":
                    Boolean boul = resultSet.getBoolean(this.columns.get(i));
                    method.invoke(obj, boul);
                    break;
                default:
            }
            i++;
        }
    }
}
