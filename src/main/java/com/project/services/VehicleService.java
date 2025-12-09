package com.project.services;


import com.project.dto.VehicleAddDTO;
import com.project.dto.VehicleResponseDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.exception.ResourceNotFound;
import com.project.model.Vehicle;
import com.project.repository.VehicleRepo;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    // add new vehicle
    public VehicleResponseDTO add(VehicleAddDTO newVehicle) {

        Vehicle vehicle = new Vehicle();
        modelMapper.map(newVehicle, vehicle);
        Vehicle saved = vehicleRepo.save(vehicle);

        logger.info("New Vehicle added with id: " + saved.getVehicleId() + " !!!");
        return modelMapper.map(saved, VehicleResponseDTO.class);
    }


    // get by id
    public VehicleResponseDTO getById(Long id) {

        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Vehicle not exist with Vehicle Id: " + id));

        return modelMapper.map(vehicle, VehicleResponseDTO.class);
    }

    // delete by id
    public String vehicleDelete(Long id) {

        if (!vehicleRepo.existsById(id)) {
            throw new ResourceNotFound("Vehicle not found !!");
        }
        vehicleRepo.deleteById(id);

        logger.info("Vehicle removed with id: " + id + " !!!");

        return "vehicle deleted with id " + id;
    }

    // update by id
    public VehicleResponseDTO updateVehicle(Long id, VehicleAddDTO newDetails) {

        Vehicle existing = vehicleRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Vehicle not exist with Vehicle Id: " + id));

        if (newDetails.getCity() != null) {
            existing.setCity(newDetails.getCity());
        }
        if (newDetails.getRegistrationNo() != null) {
            existing.setRegistrationNo(newDetails.getRegistrationNo());
        }
        if (newDetails.getBrand() != null) {
            existing.setBrand(newDetails.getBrand());
        }
        if (newDetails.getModel() != null) {
            existing.setModel(newDetails.getModel());
        }
        if (newDetails.getType() != null) {
            existing.setType(newDetails.getType());
        }
        if (newDetails.getColor() != null) {
            existing.setColor(newDetails.getColor());
        }
        if (newDetails.getCapacity() != null) {
            existing.setCapacity(newDetails.getCapacity());
        }
        if (newDetails.getYearOfManufacture() != null) {
            existing.setYearOfManufacture(newDetails.getYearOfManufacture());
        }
        if (newDetails.getChargePerDay() != 0.0) {
            existing.setChargePerDay(newDetails.getChargePerDay());
        }
        if (newDetails.getVehicleCondition() != null) {
            existing.setVehicleCondition(newDetails.getVehicleCondition());
        }
        if (newDetails.getInsuranceDate() != null) {
            existing.setInsuranceDate(newDetails.getInsuranceDate());
        }
        if (newDetails.getFuelType() != null) {
            existing.setFuelType(newDetails.getFuelType());
        }
        if (newDetails.getProvidedKm() != 0.0) {
            existing.setProvidedKm(newDetails.getProvidedKm());
        }


        logger.info("Vehicle details updated with id: " + existing.getVehicleId());

        Vehicle vehicle = vehicleRepo.save(existing);
        return modelMapper.map(vehicle, VehicleResponseDTO.class);

    }

}
