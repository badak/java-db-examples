package net.ahexample.hsqldb;

import java.math.BigDecimal;

public class NutritionInfo {
    private String name;
    private BigDecimal amount;
    private String unit;


    public String toString() {
        return name + ": " + amount + " " + unit;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
