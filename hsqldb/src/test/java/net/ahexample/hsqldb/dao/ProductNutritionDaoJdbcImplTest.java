package net.ahexample.hsqldb.dao;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;


public class ProductNutritionDaoJdbcImplTest extends BaseProductNutritionDaoTest {
    private static final String HSQLDB_JDBC_DRIVER_CLASS = "org.hsqldb.jdbc.JDBCDriver";
    private static final String HSQLDB_JDBC_URL_IN_MEMORY = "jdbc:hsqldb:mem:mymemdb";
    private static final String HSQLDB_USER = "SA";
    private static final String HSQLDB_PASSWORD = "";


    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException {
        Class.forName(HSQLDB_JDBC_DRIVER_CLASS);

        initDatabase();
    }


    @Test
    public void getNutritionInfoWithProductCode() throws SQLException {
        final ProductNutritionDaoJdbcImpl dao = new ProductNutritionDaoJdbcImpl();
        final Connection connection = getConnection();

        dao.setConnection(connection);
        getNutritionInfoWithProductCode(dao);
    }


    @Test
    public void getNutritionInfoWithProductCodeAndName() throws SQLException {
        final ProductNutritionDaoJdbcImpl dao = new ProductNutritionDaoJdbcImpl();
        final Connection connection = getConnection();

        dao.setConnection(connection);
        getNutritionInfoWithProductCodeAndName(dao);
    }


    @Test
    public void getAllProductNutritionInfo() throws SQLException {
        final ProductNutritionDaoJdbcImpl dao = new ProductNutritionDaoJdbcImpl();
        final Connection connection = getConnection();

        dao.setConnection(connection);
        getAllProductNutritionInfo(dao);
    }


    private static void initDatabase() throws SQLException, IOException {
        try (
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
        ) {
            for (String sql : getSqlInitStatements().split(STATEMENT_SEPARATOR)) {
                if (sql.length() > 0) {
                    try {
                        statement.execute(sql);
                    }
                    catch (Exception e) {
                        System.err.println("Failed to execute SQL statement " + sql);
                        throw e;
                    }
                }
            }
        }
    }


    private static String getSqlInitStatements() throws IOException {
        try (
            final InputStream is = ProductNutritionDaoJdbcImplTest.class.getResourceAsStream(INIT_DB_FILE_NAME);
        ) {
            final byte[] buf = new byte[4096];
            String retval = "";

            while (is.read(buf) > 0) {
                retval += new String(buf);
            }

            return retval.trim();
        }
    }


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(HSQLDB_JDBC_URL_IN_MEMORY, HSQLDB_USER, HSQLDB_PASSWORD);
    }
}
