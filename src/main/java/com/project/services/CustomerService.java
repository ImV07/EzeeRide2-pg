package com.project.services;

import com.project.dto.*;
import com.project.security.SecurityUtil;
import com.sun.jdi.request.DuplicateRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.enums.Role;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFound;
import com.project.model.Customer;
import com.project.repository.CustomerRepo;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /// ///////////////////

    // save customer
    @Transactional
    public CustomerDTO register(RegisterRequestDTO dto) {

        String email = dto.getEmail().toLowerCase();

        if (customerRepo.findByEmail(email).isPresent()) {
            throw new DuplicateRequestException("Customer already registered with email: " + email);
        }

        try {
            Customer customer = new Customer();
            customer.setCname(dto.getCname());
            customer.setEmail(email);
            customer.setPassword(passwordEncoder.encode(dto.getPassword()));
            customer.setContact(dto.getContact());
            customer.setRole(Role.USER);

            Customer saved = customerRepo.save(customer);

            logger.info("New customer saved with Id: " + saved.getCustomerId());

            return modelMapper.map(saved, CustomerDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Email or contact already exists.");
        } catch (Exception e) {
            throw new BadRequestException("Invalid customer data: " + e.getMessage());
        }
    }

    /// ///////////////////

    public Customer getEntityById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + id));
    }


    /// ///////////////////

    // delete customer
    public String deleteCustomer(Long id) {
        Customer tempCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer not found with id: " + id));

        customerRepo.delete(tempCustomer);

        logger.info("customer deleted with id: " + tempCustomer.getCustomerId());

        return "customer deleted with id " + id;
    }

    /// ///////////////////

    // update details
    public CustomerDTO updateCustomer(Long id, CustomerUpdateDTO updatedDetails) {

        Customer existingData = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("customer not exist with id: " + id));

        SecurityUtil.validateAccess(existingData);

        if (updatedDetails.getCname() != null && !updatedDetails.getCname().isBlank()) {
            existingData.setCname(updatedDetails.getCname());
        }

        if (updatedDetails.getContact() != null && !updatedDetails.getContact().isBlank()) {
            existingData.setContact(updatedDetails.getContact());
        }

        Customer saved = customerRepo.save(existingData);

        logger.info("customer with id: " + saved.getCustomerId() + " updates details !!!");

        return modelMapper.map(saved, CustomerDTO.class);
    }

    /// ///////////////////

    // verify customer
    public String verify(LogInRequestDTO logInDTO) {

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(logInDTO.getEmail(), logInDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(logInDTO.getEmail());
        }
        throw new BadCredentialsException("Login failed");
    }

    /// ///////////////////

    // Add new admin
    public CustomerDTO createAdmin(RegisterRequestDTO newAdmin) {

        Customer admin = new Customer();

        admin.setCname(newAdmin.getCname());
        admin.setEmail(newAdmin.getEmail());
        admin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        admin.setContact(newAdmin.getContact());
        admin.setRole(Role.ADMIN);

        Customer saved = customerRepo.save(admin);
        logger.info("New ADMIN saved with Id:" + admin.getCustomerId());

        return modelMapper.map(saved, CustomerDTO.class);

    }

    @PostConstruct
    public void init() {
        String Email = "admin1@gmail.com";

        if (customerRepo.findByEmail(Email).isEmpty()) {
            RegisterRequestDTO admin = new RegisterRequestDTO();
            admin.setCname("Admin1");
            admin.setEmail(Email);
            admin.setPassword("admin1");
            createAdmin(admin);
            logger.info("Admin created...");
        } else {
            logger.info("Admin present...");
        }

    }

    /// ///////////////////

    // update password
    public void resetPassword(Long customerId, PasswordUpdateDTO dto) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFound("customer not exist with this id."));

        SecurityUtil.validateAccess(customer);

        if (!customer.getEmail().equalsIgnoreCase(dto.getEmail())) {
            throw new BadRequestException("Entered email does not match with registered email");
        }

        customer.setPassword(passwordEncoder.encode(dto.getNewRawPassword()));

        customerRepo.save(customer);

    }

}
