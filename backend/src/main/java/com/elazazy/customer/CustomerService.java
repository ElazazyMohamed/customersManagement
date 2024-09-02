package com.elazazy.customer;

import com.elazazy.exception.DuplicateResourceException;
import com.elazazy.exception.RequestValidationException;
import com.elazazy.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer with id " + id + " does not exist"
                        )
                );
    }

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // Check if email exists
        if(customerDao.existsPersonByEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        //add customer
        customerDao.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender()
                )
        );
    }

    public void deleteCustomer(Long customerId) {
        if(!customerDao.existsPersonById(customerId)) {
            throw new ResourceNotFoundException("Customer with id " + customerId + " does not exist");
        } else {
            customerDao.deleteCustomerById(customerId);
        }
    }

    public void updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = customerDao.selectCustomerById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer with id " + customerId + " does not exist"));
        String newCustomerName = customerUpdateRequest.name();
        String newCustomerEmail = customerUpdateRequest.email();
        Integer newCustomerAge = customerUpdateRequest.age();
        Gender newCustomerGender = customerUpdateRequest.gender();
        boolean change = false;

        // check name
        if (newCustomerName != null && !newCustomerName.isEmpty() &&
                !newCustomerName.equals(customer.getName())) {
            customer.setName(newCustomerName);
            change = true;
        }

        // check email
        if (newCustomerEmail != null && !newCustomerEmail.isEmpty() &&
                !newCustomerEmail.equals(customer.getEmail())) {
            if (customerDao.existsPersonByEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(newCustomerEmail);
            change = true;
        }

        // check age
        if (newCustomerAge != null && !newCustomerAge.equals(customer.getAge())) {
            customer.setAge(newCustomerAge);
            change = true;
        }

        // check gender
        if (newCustomerGender != null && !newCustomerGender.equals(customer.getGender())) {
            customer.setGender(newCustomerGender);
            change = true;
        }

        if (!change) {
            throw new RequestValidationException("No field to update");
        }
        customerDao.editCustomer(customer);
    }
}
