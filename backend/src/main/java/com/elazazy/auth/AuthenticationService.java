package com.elazazy.auth;

import com.elazazy.customer.Customer;
import com.elazazy.customer.CustomerDTO;
import com.elazazy.customer.CustomerDTOMapper;
import com.elazazy.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            CustomerDTOMapper customerDTOMapper,
            JWTUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
        Customer principle = (Customer) authentication.getPrincipal();
        CustomerDTO customerDTO = customerDTOMapper.apply(principle);
        String token = jwtUtil.issueToken(customerDTO.email(), "ROLE_USER");
        return new AuthenticationResponse(token, customerDTO);
    }
}
