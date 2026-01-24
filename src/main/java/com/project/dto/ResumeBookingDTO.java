package com.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class ResumeBookingDTO {

    private Long bookingId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private String destination;
    private int groupSize;
    private List<VehicleDTO> availableVehicles;
    private String message;

    public ResumeBookingDTO(Long bookingId, LocalDate startDate, LocalDate endDate, String destination, int groupSize, List<VehicleDTO> availableVehicles, String message) {
        this.bookingId = bookingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.groupSize = groupSize;
        this.availableVehicles = availableVehicles;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<VehicleDTO> getAvailableVehicles() {
        return availableVehicles;
    }

    public void setAvailableVehicles(List<VehicleDTO> availableVehicles) {
        this.availableVehicles = availableVehicles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
