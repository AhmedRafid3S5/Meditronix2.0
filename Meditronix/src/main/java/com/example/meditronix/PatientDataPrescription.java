package com.example.meditronix;

public class PatientDataPrescription {

    private final String name;
    private final String age;
    private final String gender;

    public PatientDataPrescription(String name, String age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    // Getters for name, age, and gender
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
