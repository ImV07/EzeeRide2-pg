package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.enums.FuelType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class VehicleAddDTO {

    @NotBlank
    private String city;

    @NotBlank
    private String registrationNo;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String type;

    @NotBlank
    private String color;

    @Min(value = 1)
    private Integer capacity;

    @NotNull
    private Integer yearOfManufacture;

    @NotNull
    @Positive
    private double chargePerDay;

    @NotBlank
    private String vehicleCondition;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate insuranceDate;

    @NotNull
    private FuelType fuelType;

    @PositiveOrZero(message = "Provided KM cannot be negative")
    private double providedKm;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getVehicleCondition() {
        return vehicleCondition;
    }

    public void setVehicleCondition(String vehicleCondition) {
        this.vehicleCondition = vehicleCondition;
    }

    public LocalDate getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(LocalDate insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public double getChargePerDay() {
        return chargePerDay;
    }

    public void setChargePerDay(double chargePerDay) {
        this.chargePerDay = chargePerDay;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(Integer yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public double getProvidedKm() {
        return providedKm;
    }

    public void setProvidedKm(double providedKm) {
        this.providedKm = providedKm;
    }
}
