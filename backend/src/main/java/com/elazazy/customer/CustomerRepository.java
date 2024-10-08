package com.elazazy.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsCustomerById(Long id);
    boolean existsCustomerByEmail(String email);
}
