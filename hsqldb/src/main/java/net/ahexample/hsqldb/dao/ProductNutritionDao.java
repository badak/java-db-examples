package net.ahexample.hsqldb.dao;

import java.sql.SQLException;
import java.util.List;

import net.ahexample.hsqldb.NutritionInfo;
import net.ahexample.hsqldb.ProductNutritionInfo;


public interface ProductNutritionDao {
    List<NutritionInfo> getNutritionInfo(String productCode) throws SQLException;

    List<NutritionInfo> getNutritionInfo(String productCode, String name) throws SQLException;

    List<ProductNutritionInfo> getAllProductNutritionInfo() throws SQLException;
}
