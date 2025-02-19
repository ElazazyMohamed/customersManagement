package com.elazazy.auth;

import com.elazazy.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
