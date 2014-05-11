package net.ahexample.hsqldb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ahexample.hsqldb.NutritionInfo;
import net.ahexample.hsqldb.ProductNutritionInfo;


public class ProductNutritionDaoJdbcImpl implements ProductNutritionDao {
    private Connection connection;


    @Override
    public List<NutritionInfo> getNutritionInfo(String productCode) throws SQLException {
        final List<NutritionInfo> retval = new ArrayList<>();

        try (
            final PreparedStatement statement = connection.prepareCall("{ call GetNutritionInfo(?) }");
        ) {
            statement.setString(1, productCode);

            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    final NutritionInfo ni = new NutritionInfo();

                    ni.setName(rs.getString("Name"));
                    ni.setAmount(rs.getBigDecimal("Amount"));
                    ni.setUnit(rs.getString("Unit"));

                    retval.add(ni);
                }
            }
        }

        return retval;
    }


    @Override
    public List<NutritionInfo> getNutritionInfo(String productCode, String name) throws SQLException {
        final List<NutritionInfo> retval = new ArrayList<>();

        try (
            final PreparedStatement statement = connection.prepareCall("{ call GetNutritionInfo(?, ?) }");
        ) {
            statement.setString(1, productCode);
            statement.setString(2, name);

            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    final NutritionInfo ni = new NutritionInfo();

                    ni.setName(rs.getString("Name"));
                    ni.setAmount(rs.getBigDecimal("Amount"));
                    ni.setUnit(rs.getString("Unit"));

                    retval.add(ni);
                }
            }
        }

        return retval;
    }


    public List<ProductNutritionInfo> getAllProductNutritionInfo() throws SQLException {
        final List<ProductNutritionInfo> retval = new ArrayList<>();

        try (
            final PreparedStatement statement = connection.prepareCall("{ call GetAllNutritionInfo() }");
        ) {
            final Map<String, List<NutritionInfo>> nutritionInfoMap = new HashMap<>();

            // first result set
            try (final ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    final ProductNutritionInfo pni = new ProductNutritionInfo();

                    pni.setCode(rs.getString("Code"));
                    pni.setName(rs.getString("Name"));
                    pni.setEntryDate(rs.getDate("EntryDate"));
                    pni.setAmount(rs.getBigDecimal("Amount"));
                    pni.setUnit(rs.getString("Unit"));
                    pni.setQuantity(rs.getInt("Quantity"));
                    pni.setStdNutritionAmount(rs.getBigDecimal("StdAmount"));
                    pni.setStdNutritionUnit(rs.getString("StdUnit"));

                    retval.add(pni);
                }
            }

            // second result set
            if (statement.getMoreResults()) {
                try (final ResultSet rs = statement.getResultSet()) {
                    while (rs.next() ) {
                        final NutritionInfo ni = new NutritionInfo();
                        final String productCode = rs.getString("ProductCode");

                        ni.setName(rs.getString("Name"));
                        ni.setAmount(rs.getBigDecimal("Amount"));
                        ni.setUnit(rs.getString("Unit"));

                        getNutritionInfoList(nutritionInfoMap, productCode).add(ni);
                    }
                }

                retval.forEach(pni -> {
                    pni.setNutritionInfoList(nutritionInfoMap.get(pni.getCode()));
                });
            }
        }

        return retval;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    private List<NutritionInfo> getNutritionInfoList(Map<String, List<NutritionInfo>> map, String productCode) {
        List<NutritionInfo> retval = map.get(productCode);

        if (retval == null) {
            retval = new ArrayList<>();
            map.put(productCode, retval);
        }

        return retval;
    }
}
