package com.project.controllers;

import com.project.dto.VehicleAddDTO;
import com.project.dto.VehicleResponseDTO;
import com.project.repository.VehicleRepo;
import com.project.util.PageMapper;
import com.project.util.PageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Vehicle;
import com.project.services.VehicleService;

@RestController
@RequestMapping("api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public VehicleResponseDTO postVehicle(@RequestBody VehicleAddDTO newVehicle) {
        return service.add(newVehicle);
    }

    //	get all
    @GetMapping
    public PageResponse<VehicleResponseDTO> restGetALL(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> pageDate = vehicleRepo.findAll(pageable);

        return PageMapper.map(pageDate, vehicle -> modelMapper.map(vehicle, VehicleResponseDTO.class));

    }

    //	get by regNo{for state}
    @GetMapping("/state")
    public PageResponse<VehicleResponseDTO> restGetByState(@RequestParam String stateCode,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> pageDate = vehicleRepo.findAllByRegistrationNo(stateCode.toUpperCase(), pageable);

        return PageMapper.map(pageDate, vehicle -> modelMapper.map(vehicle, VehicleResponseDTO.class));

    }

    //	get by vehicleId
    @GetMapping("/{id}")
    public VehicleResponseDTO restGetById(@PathVariable Long id) {

        return service.getById(id);

    }

    //	remove by id
    @DeleteMapping("/remove/{id}")
    public String restDeleteById(@PathVariable Long id) {

        return service.vehicleDelete(id);

    }

    //	patch update by id
    @PatchMapping("/update/{id}")
    public VehicleResponseDTO restupdateById(@PathVariable Long id, @RequestBody VehicleAddDTO newDetails) {

        return service.updateVehicle(id, newDetails);
    }

}
