package com.project.dto;

import com.project.enums.FuelType;

import java.time.LocalDate;

public class VehicleResponseDTO {

    private Long vehicleId;

    private String city;

    private String registrationNo;

    private String brand;

    private String model;

    private String type;

    private String color;

    private Integer capacity;

    private Integer yearOfManufacture;

    private boolean available;

    private double chargePerDay;

    private String vehicleCondition;

    private LocalDate insuranceDate;

    private FuelType fuelType;

    private double providedKm;

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(Integer yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getChargePerDay() {
        return chargePerDay;
    }

    public void setChargePerDay(double chargePerDay) {
        this.chargePerDay = chargePerDay;
    }

    public String getVehicleCondition() {
        return vehicleCondition;
    }

    public void setVehicleCondition(String vehicleCondition) {
        this.vehicleCondition = vehicleCondition;
    }

    public double getProvidedKm() {
        return providedKm;
    }

    public void setProvidedKm(double providedKm) {
        this.providedKm = providedKm;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public LocalDate getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(LocalDate insuranceDate) {
        this.insuranceDate = insuranceDate;
    }
}
