package com.elazazy.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsPersonByEmail(String email);
    void deleteCustomerById(Long id);
    boolean existsPersonById(Long id);
    void editCustomer(Customer customer);
}
