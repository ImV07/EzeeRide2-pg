package com.project.controllers;

import com.project.dto.*;
import com.project.enums.BookingStatus;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFound;
import com.project.model.Booking;
import com.project.model.Customer;
import com.project.repository.BookingRepo;
import com.project.security.SecurityUtil;
import com.project.services.BookingService;
import com.project.services.EmailService;
import com.project.util.PageMapper;
import com.project.util.PageResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private EmailService emailSender;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    /// ///////////////////////
    // Get All Booking
    @GetMapping
    public PageResponse<BookingDTO> restGetAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> pageDate = bookingRepo.findAll(pageable);

        return PageMapper.map(pageDate, booking -> modelMapper.map(booking, BookingDTO.class));
    }


    /// ///////////////////////

    // Fetch vehicles for destination
    @PostMapping("/initiate/{customerId}")
    public AvailableVehicleDTO initiateBooking(@PathVariable Long customerId, @RequestBody InitialBookingDTO basicDetails) {
        return bookingService.initiateBooking(customerId, basicDetails);
    }

    /// ///////////////////////

    @GetMapping("/resume/{bookingId}")
    public ResumeBookingDTO viewBooking(@PathVariable Long bookingId) {

        return bookingService.resumeBooking(bookingId);

    }

    /// ///////////////////////

    // customer select's vehicles for booking
    @PostMapping("/select-vehicle/{bookingId}")
    public BookingDTO selectVehicle(@PathVariable Long bookingId, @RequestBody VehicleSelectionDTO dto) {

        return bookingService.vehicleSelection(bookingId, dto);

    }

    /// ///////////////////////


    // [confirm after pay] / [cancel with reason]
    @Transactional
    @PatchMapping("/payment-status/{bookingId}")
    public String updatePaymentStatus(@PathVariable Long bookingId, @RequestBody PaymentUpdateDTO paymentUpdate) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFound("Booking not found with booking ID: " + bookingId));

        SecurityUtil.validateAccess(booking.getCustomer());

        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new BadRequestException("BOOKING IS CANCELLED, FURTHER PROCESS NOT POSSIBLE !!!");
        }

        booking.setPaymentStatus(paymentUpdate.isPaymentStatus());

        String toEmail = booking.getCustomer().getEmail();

        if (paymentUpdate.isPaymentStatus()) {
            booking.setStatus(BookingStatus.CONFIRM);
            booking.setCancelReason(null);

            booking.getVehicle().forEach(vehicle -> vehicle.setAvailable(false));

            String sub1 = "Booking Confirmed";
            String body1 = booking.getCustomer().getCname() + "\n\nYour booking is confirmed with bookingId: "
                    + booking.getBookingId() + "\nYour customerId is: " + booking.getCustomer().getCustomerId()
                    + "\n\nEzeeRide";

            emailSender.sendEmail(toEmail, sub1, body1);
            logger.info("Confirmation mail sent to customer: " + toEmail);

        } else {

            if (paymentUpdate.getCancelReason() == null) {
                throw new BadRequestException("Cancel reason required");
            }

            booking.setStatus(BookingStatus.CANCELED);
            booking.setCancelReason(paymentUpdate.getCancelReason());
            booking.getVehicle().forEach(vehicle -> vehicle.setAvailable(true));

            String sub2 = "Booking Cancelled";
            String body2 = booking.getCustomer().getCname() + "\n\nYour booking is cancelled with bookingId: "
                    + booking.getBookingId() + "\n\nBooking Amount of Rs" + booking.getAmount()
                    + " refunded successfully !!!" + "\n\nEzeeRide";

            emailSender.sendEmail(toEmail, sub2, body2);
            logger.info("Canceled/Refund mail sent to customer: " + toEmail);

        }

        bookingRepo.save(booking);
        return "Booking updated. Current status: " + booking.getStatus();
    }

    /// ///////////////////////


    //	get by bookingId
    @GetMapping("/{bookingId}")
    public BookingDTO getById(@PathVariable Long bookingId) {

        Booking existingBooking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFound("Booking not found with booking ID " + bookingId));

        Customer customer = existingBooking.getCustomer();

        SecurityUtil.validateAccess(customer);

        BookingDTO bookingDTO = modelMapper.map(existingBooking, BookingDTO.class);

        List<VehicleDTO> vehicleDTOs = existingBooking.getVehicle().stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class)).collect(Collectors.toList());

        bookingDTO.setVehicle(vehicleDTOs);
        bookingDTO.setMessage("Your booking information");
        logger.info("ADMIN / User: " + customer.getCname() + " checked booking");
        return bookingDTO;
    }

    /// ///////////////////////


    //	update destination by bookingId
    @PatchMapping("/update-destination/{bookingId}")
    public AvailableVehicleDTO updateDestination(@PathVariable Long bookingId, @RequestBody InitialBookingDTO updateBooking) {

        return bookingService.updateDestination(bookingId, updateBooking);

    }

}