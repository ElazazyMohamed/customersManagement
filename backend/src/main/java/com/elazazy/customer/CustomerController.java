package com.elazazy.customer;

import com.elazazy.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.registerCustomer(request);
        String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @GetMapping()
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO getCustomer(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}
