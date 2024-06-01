package com.example.meditronix;

import java.sql.Date;
import java.sql.Time;

public class Prescription {
    private String prescriptionCode;
    private Date generatedDate;
    private Time generatedTime;

    public Prescription(String prescriptionCode, Date generatedDate, Time generatedTime) {
        this.prescriptionCode = prescriptionCode;
        this.generatedDate = generatedDate;
        this.generatedTime = generatedTime;
    }

    public String getPrescriptionCode() {
        return prescriptionCode;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public Time getGeneratedTime() {
        return generatedTime;
    }
}