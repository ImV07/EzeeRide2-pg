package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Customer;

import java.util.Optional;


@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long>  {

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByCname(String cname);

}
