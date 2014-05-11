package net.ahexample.hsqldb;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class ProductNutritionInfo {
    private String code;
    private String name;
    private Date entryDate;
    private BigDecimal amount;
    private String unit;
    private int quantity;
    private BigDecimal stdNutritionAmount;
    private String stdNutritionUnit;
    private List<NutritionInfo> nutritionInfoList;


    public String toString() {
        return code + " (" + name + ", " + entryDate + "): " + amount + " " + unit + " [" + quantity + "]"
                + " ,standard measurement: " + stdNutritionAmount + " " + stdNutritionUnit
                + " ,nutrition info: " + nutritionInfoList;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getStdNutritionAmount() {
        return stdNutritionAmount;
    }

    public void setStdNutritionAmount(BigDecimal stdNutritionAmount) {
        this.stdNutritionAmount = stdNutritionAmount;
    }

    public String getStdNutritionUnit() {
        return stdNutritionUnit;
    }

    public void setStdNutritionUnit(String stdNutritionUnit) {
        this.stdNutritionUnit = stdNutritionUnit;
    }

    public List<NutritionInfo> getNutritionInfoList() {
        return nutritionInfoList;
    }

    public void setNutritionInfoList(List<NutritionInfo> nutritionInfoList) {
        this.nutritionInfoList = nutritionInfoList;
    }

}
