package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.model.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDate;

public class ServiceRequestDTO {

    private String serviceDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate serviceDate;

    private double servicingCost;

    private String remarks;

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public double getServicingCost() {
        return servicingCost;
    }

    public void setServicingCost(double servicingCost) {
        this.servicingCost = servicingCost;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
