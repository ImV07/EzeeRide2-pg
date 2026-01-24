package com.project.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.project.dto.*;
import com.project.exception.BookingStatusException;
import com.project.exception.NoVehiclesAvailableException;
import com.project.security.SecurityUtil;
import com.project.util.TextNormalizer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.enums.BookingStatus;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFound;
import com.project.model.Booking;
import com.project.model.Customer;
import com.project.model.Vehicle;
import com.project.repository.BookingRepo;
import com.project.repository.CustomerRepo;
import com.project.repository.VehicleRepo;

@Service
public class BookingService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    /// //////////////////

    // Initial booking
    public AvailableVehicleDTO initiateBooking(Long cid, InitialBookingDTO initialDetails) {
        Customer customer = customerRepo.findById(cid)
                .orElseThrow(() -> new ResourceNotFound("Customer not found"));

        SecurityUtil.validateAccess(customer);

        long days = ChronoUnit.DAYS.between(initialDetails.getStartDate(), initialDetails.getEndDate());
        if (days <= 0) throw new IllegalArgumentException("End date must be after start date");

        String normalizedDestination = TextNormalizer.upper(initialDetails.getDestination());

        List<Vehicle> availableVehicles = vehicleRepo.findAvailableVehiclesByCity(normalizedDestination);

        if (availableVehicles.isEmpty()) {
            throw new NoVehiclesAvailableException("No vehicles available for " + normalizedDestination);
        }

        List<VehicleDTO> vehicleDTOs = availableVehicles.stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .toList();


        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setDateOfBooking(LocalDate.now());
        booking.setStartDate(initialDetails.getStartDate());
        booking.setEndDate(initialDetails.getEndDate());
        booking.setDestination(normalizedDestination);
        booking.setGroupSize(initialDetails.getGroupSize());

        bookingRepo.save(booking);


        return new AvailableVehicleDTO(booking.getBookingId(), normalizedDestination, vehicleDTOs, "available vehicles for the destination: " + normalizedDestination);
    }


    /// ///////////////

    // Select vehicle from Available Vehicles
    public BookingDTO vehicleSelection(Long bid, VehicleSelectionDTO dto) {

        Booking booking = bookingRepo.findById(bid)
                .orElseThrow(() -> new ResourceNotFound("bookinId not (found / registered) in db "));

        SecurityUtil.validateAccess(booking.getCustomer());

        List<Vehicle> selectedVehicles = vehicleRepo.findAllById(dto.getVehicleIds());

        for (Vehicle vehicle : selectedVehicles) {
            List<Booking> conflicts = bookingRepo.findBookingsConflicts(
                    vehicle.getVehicleId(),
                    booking.getStartDate(),
                    booking.getEndDate()
            );

            if (!conflicts.isEmpty()) {
                throw new BadRequestException("Vehicle ID " + vehicle.getVehicleId() + " is already booked from " +
                        conflicts.get(0).getStartDate() + " to " + conflicts.get(0).getEndDate());
            }
        }

        // Calculate number of days
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        double totalAmount = selectedVehicles.stream().mapToDouble(v -> v.getChargePerDay() * days).sum();

        booking.setAmount(totalAmount);
        booking.setVehicle(selectedVehicles);
        Booking savedBooking = bookingRepo.save(booking);

        BookingDTO bookingDTO = modelMapper.map(savedBooking, BookingDTO.class);

        List<VehicleDTO> vehicleDTOList = selectedVehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).collect(Collectors.toList());

        bookingDTO.setVehicle(vehicleDTOList);
        bookingDTO.setMessage("Booking will confirm after payment!!");

        return bookingDTO;
    }

    /// /////////////////

//	update destination and fetch available vehicles
    public AvailableVehicleDTO updateDestination(Long bookingId, InitialBookingDTO newDestination) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFound("booking not found with ID: " + bookingId));

        SecurityUtil.validateAccess(booking.getCustomer());

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Destination can only be changed if booking is PENDING.");
        }

        String normalizedNewDestination = TextNormalizer.upper(newDestination.getDestination());

        booking.setDestination(normalizedNewDestination);
        booking.setStartDate(newDestination.getStartDate());
        booking.setEndDate(newDestination.getEndDate());
        booking.setGroupSize(newDestination.getGroupSize());
        bookingRepo.save(booking);

        List<Vehicle> availableVehicles = vehicleRepo.findAvailableVehiclesByCity(normalizedNewDestination);
        List<VehicleDTO> vehicleDTOs = availableVehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).collect(Collectors.toList());


        return new AvailableVehicleDTO(bookingId, normalizedNewDestination, vehicleDTOs, "available vehicles for the destination: " + normalizedNewDestination);

    }

    //Resume booking
    public ResumeBookingDTO resumeBooking(Long bid) {

        Booking booking = bookingRepo.findById(bid)
                .orElseThrow(() -> new ResourceNotFound("Initial Booking not done."));

        SecurityUtil.validateAccess(booking.getCustomer());

        if (booking.getStatus() != BookingStatus.PENDING) {

            throw new BookingStatusException("Booking cannot resumed. Current Status is:"+booking.getStatus());
        }


        String normalizedDestination = booking.getDestination();

        List<Vehicle> availableVehicles =
                vehicleRepo.findAvailableVehiclesByCity(normalizedDestination);

        if (availableVehicles.isEmpty()) {
            throw new NoVehiclesAvailableException(
                    "Currently no vehicles available for " + normalizedDestination);
        }

        List<VehicleDTO> vehicleDTOs = availableVehicles.stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .toList();

        return new ResumeBookingDTO(
                booking.getBookingId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getDestination(),
                booking.getGroupSize(),
                vehicleDTOs,
                "Available vehicles for destination: " + booking.getDestination()
        );
    }

}