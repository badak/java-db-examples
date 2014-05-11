package net.ahexample.hsqldb.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ahexample.hsqldb.NutritionInfo;
import net.ahexample.hsqldb.ProductNutritionInfo;


public class BaseProductNutritionDaoTest {
    /** Statement separator to get around HSQLDB's semicolon statement delimiter in procedures. */
    static final String STATEMENT_SEPARATOR = ";;";

    /** Name of file containing SQL statements to initialise the database. */
    static final String INIT_DB_FILE_NAME = "productNutrition.sql";


    void getNutritionInfoWithProductCodeAndName(final ProductNutritionDao dao) throws SQLException {
        final List<NutritionInfo> niList;

        niList = dao.getNutritionInfo("BB1", "Fat");
        assertThat("product nutrition info list - not null", niList, notNullValue());
        assertThat("product nutrition info list - size", niList.size(), equalTo(1));
        checkNutritionInfo(niList.get(0), "Fat", new BigDecimal("4.8"), "g");
    }


    void getNutritionInfoWithProductCode(final ProductNutritionDao dao) throws SQLException {
        final List<NutritionInfo> niList;

        niList = dao.getNutritionInfo("CB1");
        assertThat("product nutrition info list - not null", niList, notNullValue());
        assertThat("product nutrition info list - size", niList.size(), equalTo(4));
        checkNutritionInfo(niList.get(0), "Carbohydrate", new BigDecimal("54"), "g");
        checkNutritionInfo(niList.get(1), "Energy", new BigDecimal("2200"), "kJ");
        checkNutritionInfo(niList.get(2), "Fat", new BigDecimal("24.5"), "g");
        checkNutritionInfo(niList.get(3), "Protein", new BigDecimal("12.3"), "g");
    }


    void getAllProductNutritionInfo(final ProductNutritionDao dao) throws SQLException {
        final List<ProductNutritionInfo> pniList;
        ProductNutritionInfo pni;
        List<NutritionInfo> niList;
        pniList = dao.getAllProductNutritionInfo();
        assertThat("infoList not null", pniList, notNullValue());
        assertThat("infoList size", pniList.size(), equalTo(3));

        pni = pniList.get(0);
        checkProductNutritionInfo(pni, "BB1", "Breakfast Bar 1", makeDate(1, 2, 2014),
                                    new BigDecimal("200"), "g", 6, new BigDecimal("100"), "g");
        niList = pni.getNutritionInfoList();
        assertThat("product nutrition info list - not null", niList, notNullValue());
        assertThat("product nutrition info list - size", niList.size(), equalTo(3));
        checkNutritionInfo(niList.get(0), "Energy", new BigDecimal("1400"), "kJ");
        checkNutritionInfo(niList.get(1), "Fat", new BigDecimal("4.8"), "g");
        checkNutritionInfo(niList.get(2), "Sodium", new BigDecimal("210"), "mg");

        pni = pniList.get(1);
        checkProductNutritionInfo(pni, "CB1", "Chocolate Bar 1", makeDate(10, 12, 2013),
                                    new BigDecimal("49"), "g", 1, new BigDecimal("100"), "g");
        niList = pni.getNutritionInfoList();
        assertThat("product nutrition info list - not null", niList, notNullValue());
        assertThat("product nutrition info list - size", niList.size(), equalTo(4));
        checkNutritionInfo(niList.get(0), "Carbohydrate", new BigDecimal("54"), "g");
        checkNutritionInfo(niList.get(1), "Energy", new BigDecimal("2200"), "kJ");
        checkNutritionInfo(niList.get(2), "Fat", new BigDecimal("24.5"), "g");
        checkNutritionInfo(niList.get(3), "Protein", new BigDecimal("12.3"), "g");

        pni = pniList.get(2);
        checkProductNutritionInfo(pni, "MLK1", "Milk 1", makeDate(12, 4, 2014),
                                    new BigDecimal("2"), "l", 1, new BigDecimal("100"), "ml");
        niList = pni.getNutritionInfoList();
        assertThat("product nutrition info list - not null", niList, notNullValue());
        assertThat("product nutrition info list - size", niList.size(), equalTo(5));
        checkNutritionInfo(niList.get(0), "Calcium", new BigDecimal("161"), "mg");
        checkNutritionInfo(niList.get(1), "Carbohydrate", new BigDecimal("4.2"), "g");
        checkNutritionInfo(niList.get(2), "Energy", new BigDecimal("200"), "kJ");
        checkNutritionInfo(niList.get(3), "Fat", new BigDecimal("2.3"), "g");
        checkNutritionInfo(niList.get(4), "Protein", new BigDecimal("3.6"), "g");
    }


    private void checkProductNutritionInfo(ProductNutritionInfo pni, String code, String name,
                        Date entryDate, BigDecimal amount, String unit, int quantity,
                        BigDecimal stdNutritionAmount, String stdNutritionUnit) {
        final int scale = 2;

        assertThat("product code", pni.getCode(), equalTo(code));
        assertThat("product name", pni.getName(), equalTo(name));
        assertThat("product entry Date", pni.getEntryDate(), equalTo(entryDate));
        assertThat("product amount", pni.getAmount().setScale(scale), equalTo(amount.setScale(scale)));
        assertThat("product unit", pni.getUnit(), equalTo(unit));
        assertThat("product quantity", pni.getQuantity(), equalTo(quantity));
        assertThat("product standard nutrition amount", pni.getStdNutritionAmount(), equalTo(stdNutritionAmount));
        assertThat("product standard nutrition unit", pni.getStdNutritionUnit(), equalTo(stdNutritionUnit));
    }


    private void checkNutritionInfo(NutritionInfo ni, String name, BigDecimal amount, String unit) {
        final int scale = 2;

        assertThat("nutrition info name", ni.getName(), equalTo(name));
        assertThat("nutrition info amount", ni.getAmount().setScale(scale), equalTo(amount.setScale(scale)));
        assertThat("nutrition info unit", ni.getUnit(), equalTo(unit));
    }


    private Date makeDate(int date, int month, int year) {
        final Calendar cal = Calendar.getInstance();

        cal.set(year, month - 1, date, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
}
