package net.ahexample.hsqldb.dao;


import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;


public class ProductNutritionDaoSpringJdbcImplTest extends BaseProductNutritionDaoTest {
    private static EmbeddedDatabase database;
    private static JdbcTemplate jdbcTemplate;


    @BeforeClass
    public static void init() {
        database = (new EmbeddedDatabaseBuilder())
                    .setSeparator(STATEMENT_SEPARATOR)
                    .addScript("classpath:/net/ahexample/hsqldb/dao/" + INIT_DB_FILE_NAME)
                    .build();

        jdbcTemplate = new JdbcTemplate(database);
    }


    @AfterClass
    public static void shutDown() {
        database.shutdown();
    }


    @Test
    public void getNutritionInfoWithProductCode() throws SQLException {
        final ProductNutritionDaoSpringJdbcImpl dao = new ProductNutritionDaoSpringJdbcImpl();

        dao.setJdbcTemplate(jdbcTemplate);
        getNutritionInfoWithProductCode(dao);
    }


    @Test
    public void getNutritionInfoWithProductCodeAndName() throws SQLException {
        final ProductNutritionDaoSpringJdbcImpl dao = new ProductNutritionDaoSpringJdbcImpl();

        dao.setJdbcTemplate(jdbcTemplate);
        getNutritionInfoWithProductCodeAndName(dao);
    }


    @Test
    public void getAllProductNutritionInfo() throws SQLException {
        final ProductNutritionDaoSpringJdbcImpl dao = new ProductNutritionDaoSpringJdbcImpl();

        dao.setJdbcTemplate(jdbcTemplate);
        getAllProductNutritionInfo(dao);
    }
}
