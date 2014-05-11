package net.ahexample.hsqldb.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ahexample.hsqldb.NutritionInfo;
import net.ahexample.hsqldb.ProductNutritionInfo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;


public class ProductNutritionDaoSpringJdbcImpl implements ProductNutritionDao {
    private JdbcTemplate jdbcTemplate;


    @SuppressWarnings("unchecked")
    @Override
    public List<NutritionInfo> getNutritionInfo(String productCode) throws SQLException {
        final SimpleJdbcCall funcCall = new SimpleJdbcCall(jdbcTemplate)
                                            .withProcedureName("GetNutritionInfo");
        final String result1 = "result1";

        // procedure parameter definitions
        funcCall.declareParameters(new SqlParameter("productCode", Types.CHAR));

        /* method call using non-lambda expression */
        funcCall.returningResultSet(result1, new RowMapper<NutritionInfo>() {
            @Override
            public NutritionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                final NutritionInfo ni = new NutritionInfo();

                ni.setName(rs.getString("Name"));
                ni.setAmount(rs.getBigDecimal("Amount"));
                ni.setUnit(rs.getString("Unit"));

                return ni;
            }
        });

        // call using arguments for parameter values
        return (List<NutritionInfo>) funcCall.execute(productCode).get(result1);
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<NutritionInfo> getNutritionInfo(String productCode, String name) throws SQLException {
        final SimpleJdbcCall funcCall = new SimpleJdbcCall(jdbcTemplate)
                                            .withProcedureName("GetNutritionInfo");
        final String result1 = "result1";
        final Map<String, Object> paramMap = new HashMap<>();

        // procedure parameter definitions
        funcCall.declareParameters(
                    new SqlParameter("productCode", Types.CHAR),
                    new SqlParameter("name", Types.CHAR));

        /* method call using non-lambda expression */
        funcCall.returningResultSet(result1, new RowMapper<NutritionInfo>() {
            @Override
            public NutritionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                final NutritionInfo ni = new NutritionInfo();

                ni.setName(rs.getString("Name"));
                ni.setAmount(rs.getBigDecimal("Amount"));
                ni.setUnit(rs.getString("Unit"));

                return ni;
            }
        });

        // values for procedure parameters
        paramMap.put("name", name);
        paramMap.put("productCode", productCode);

        // call using parameter map
        return (List<NutritionInfo>) funcCall.execute(paramMap).get(result1);
    }


    @SuppressWarnings("unchecked")
    public List<ProductNutritionInfo> getAllProductNutritionInfo() throws SQLException {
        final String result1 = "result1";
        final String result2 = "result2";
        final SimpleJdbcCall procCall = new SimpleJdbcCall(jdbcTemplate)
                                                .withProcedureName("GetAllNutritionInfo");
        final Map<String, List<NutritionInfo>> nutritionInfoMap = new HashMap<>();
        Map<String, Object> results;
        List<ProductNutritionInfo> retval;

        /* using lambda expressions */
        // first result set
        procCall.returningResultSet(
                    result1,
                    (ResultSet rs, int rowNum) -> {
                        final ProductNutritionInfo pni = new ProductNutritionInfo();

                        pni.setCode(rs.getString("Code"));
                        pni.setName(rs.getString("Name"));
                        pni.setEntryDate(rs.getDate("EntryDate"));
                        pni.setAmount(rs.getBigDecimal("Amount"));
                        pni.setUnit(rs.getString("Unit"));
                        pni.setQuantity(rs.getInt("Quantity"));
                        pni.setStdNutritionAmount(rs.getBigDecimal("StdAmount"));
                        pni.setStdNutritionUnit(rs.getString("StdUnit"));

                        return pni;
                    });

        // second result set
        procCall.returningResultSet(
                    result2,
                    (ResultSet rs, int rowNum) -> {
                        final NutritionInfo ni = new NutritionInfo();
                        final String productCode = rs.getString("ProductCode");

                        ni.setName(rs.getString("Name"));
                        ni.setAmount(rs.getBigDecimal("Amount"));
                        ni.setUnit(rs.getString("Unit"));

                        getNutritionInfoList(nutritionInfoMap, productCode).add(ni);

                        return ni;
                    });

        results = procCall.execute();
        retval = (List<ProductNutritionInfo>) results.get(result1);

        retval.forEach(pni -> {
            pni.setNutritionInfoList(nutritionInfoMap.get(pni.getCode()));
        });

        return retval;
    }


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
