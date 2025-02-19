package com.elazazy.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
