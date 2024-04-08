package com.example.meditronix;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Medicine {
    private String Name;
    private String Dose;
    private String Expiry;
    private String Type;
    private Float price;
    private Float Quantity;
    private Float UnitCost;

    private String serial_id;

    public Medicine(String name, String dose, String expiry, String type, Float price, Float quantity, Float unitCost) {
        this.Name = name;
        this.Dose = dose;
        this.Expiry = expiry;
        this.Type = type;
        this.price = price;
        this.Quantity = quantity;
        this.UnitCost = unitCost;
    }

    public Medicine(ResultSet rs) throws SQLException {
        this.Name = rs.getString("Name");
        this.Dose = rs.getString("Dose");
        this.Expiry = rs.getString("Expiry");
        this.Type = rs.getString("Type");
        this.price = rs.getFloat("Selling_price");  // Assuming "Selling_price" is the price column
        this.Quantity = rs.getFloat("Available_Quantity");
        this.UnitCost = rs.getFloat("unit_cost");
        this.serial_id = rs.getString("serial_id");
    }

    public String getName() {
        return Name;
    }

    public String getDose() {
        return Dose;
    }

    public String getExpiry() {
        return Expiry;
    }

    public String getType() {
        return Type;
    }

    public Float getPrice() {
        return price;
    }

    public Float getQuantity() {
        return Quantity;
    }

    public Float getUnitCost() {
        return UnitCost;
    }

    public String getSerial_id(){ return serial_id;}

    public void setSerial_id(String id){
        this.serial_id = id;
    }
}
