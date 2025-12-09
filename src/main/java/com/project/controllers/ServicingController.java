package com.project.controllers;

import java.util.List;

import com.project.dto.ServiceRequestDTO;
import com.project.dto.ServiceStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ServicingDTO;
import com.project.model.Servicing;
import com.project.services.ServicingService;

@RestController
@RequestMapping("/api/service")
public class ServicingController {

    @Autowired
    private ServicingService service;


    @PostMapping("/add/{vehicleId}")
    public ServicingDTO RestPost(@PathVariable Long vehicleId, @RequestBody ServiceRequestDTO details) {

        return service.addToService(vehicleId, details);
    }

    @GetMapping
    public List<ServicingDTO> getAll() {

        return service.getAllServiceDetails();
    }


    @GetMapping("/{servicingId}")
    public Servicing get(@PathVariable Long servicingId) {

        return service.getById(servicingId);
    }


    @PatchMapping("/servicing-status/{servicingId}")
    public ServicingDTO patch(@PathVariable Long servicingId, @RequestBody ServiceStatusDTO updatedDetails) {

        return service.update(servicingId, updatedDetails);
    }


}
