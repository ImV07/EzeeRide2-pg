package com.project.controllers;

import com.project.dto.*;
import com.project.repository.CustomerRepo;
import com.project.util.PageMapper;
import com.project.util.PageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Customer;
import com.project.security.SecurityUtil;
import com.project.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

	@Autowired
	private CustomerService service;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

	// register new customers
	@PostMapping("/register")
	public ResponseEntity<CustomerDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {

        CustomerDTO response= service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// get all
	@GetMapping
	public PageResponse<CustomerDTO> restGetAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue ="4") int size) {

        Pageable pageable= PageRequest.of(page, size);
        Page<Customer> pageDate= customerRepo.findAll(pageable);

		return PageMapper.map(pageDate,customer-> modelMapper.map(customer,CustomerDTO.class));
	}

	// get by id
	@GetMapping("/{id}")
	public CustomerDTO restGetById(@PathVariable Long id) {
		return service.getById(id);
	}

	// delete by id
	@DeleteMapping("/delete/{id}")
	public String deleteById(@PathVariable Long id) {

		return service.deleteCustomer(id);
	}

	// update by id
	@PatchMapping("/update/{id}")
	public CustomerDTO restUpdate(@PathVariable Long id,@Valid @RequestBody CustomerUpdateDTO updatedDetails) {

		Customer existingCustomer = service.getEntityById(id);

		SecurityUtil.validateAccess(existingCustomer);

		return service.updateCustomer(id, updatedDetails);

	}

	// login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LogInRequestDTO logInDTO) {

        try {
            String token = service.verify(logInDTO);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

	// ADMIN can add new-admin
	@PostMapping("/add-admin")
	public CustomerDTO addAdmin(@RequestBody RegisterRequestDTO newAdmin) {
		return service.createAdmin(newAdmin);
	}

	// update password
	@PatchMapping("/updatePassword/{customerId}")
	public ResponseEntity<String> updatePassword(@PathVariable Long customerId, @RequestBody PasswordUpdateDTO dto) {
		service.resetPassword(customerId, dto);
		return ResponseEntity.ok("Password updated successfully");

	}
}
