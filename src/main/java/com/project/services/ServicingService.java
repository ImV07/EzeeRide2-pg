package com.project.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.project.dto.ServiceRequestDTO;
import com.project.dto.ServiceStatusDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ServicingDTO;
import com.project.exception.ResourceNotFound;
import com.project.model.Servicing;
import com.project.model.Vehicle;
import com.project.repository.ServicingRepo;
import com.project.repository.VehicleRepo;

@Service
public class ServicingService {

	
	@Autowired
	private ServicingRepo servicingRepo;

	@Autowired
	private VehicleRepo vehicleRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	private static final Logger logger=LoggerFactory.getLogger(ServicingService.class);
	
	//	add vehicle to service with vehicleid
	public ServicingDTO addToService(Long vid, ServiceRequestDTO details) {
		
		Vehicle vehicle=vehicleRepo.findById(vid).orElseThrow(()-> new ResourceNotFound("vehicle not found!!!"));
		
		if(!vehicle.isAvailable()) {
			throw new ResourceNotFound("vehicle is (in use /in service)");
		}
		
		vehicle.setAvailable(false);
		vehicleRepo.save(vehicle);

        Servicing servicing = mapper.map(details, Servicing.class);

        servicing.setVehicles(vehicle);
        if (servicing.getServiceDate() == null) {
            servicing.setServiceDate(LocalDate.now());
        }

        servicingRepo.save(servicing);

        ServicingDTO response = mapper.map(servicing, ServicingDTO.class);
        response.setVehicleId(vehicle.getVehicleId());

        return response;
	}

	
	//	get all service details
	public List<ServicingDTO> getAllServiceDetails() {
		return servicingRepo.findAll()
				.stream()
                .map(service -> {
                    ServicingDTO dto = mapper.map(service, ServicingDTO.class);

                    if (service.getVehicles() != null) {
                        dto.setVehicleId(service.getVehicles().getVehicleId());
                    }

                    return dto;
                })
		        .collect(Collectors.toList());
	}

	
	//	get by service id
	public Servicing getById(Long sid) {
		return servicingRepo.findById(sid).orElseThrow(()->new ResourceNotFound("Servicing id not present!!!"));
	}

	
	//	patch update by serviceid
	public ServicingDTO update(Long sid, ServiceStatusDTO updatedDetails) {
		Servicing existingService= servicingRepo.findById(sid).orElseThrow(()->new ResourceNotFound("servicing not found with id"+sid));
		

		existingService.setServiceStatus(updatedDetails.isServiceStatus());
		existingService.setPaymentStatus(updatedDetails.isPaymentStatus());
		
		Vehicle vehicle = existingService.getVehicles();

	    if (!existingService.isServiceStatus() && existingService.isPaymentStatus()) {
	        vehicle.setAvailable(true);
	    }
	    
	    logger.info("Updated servicing and vehicle availability for Vehicle ID: " + vehicle.getVehicleId());
	    
	    Servicing saved=servicingRepo.save(existingService);

        ServicingDTO dto=mapper.map(saved,ServicingDTO.class);
        dto.setVehicleId(saved.getVehicles().getVehicleId());

	    return dto;
	}
	
	
}
