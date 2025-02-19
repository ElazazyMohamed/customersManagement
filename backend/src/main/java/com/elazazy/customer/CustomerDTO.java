package com.elazazy.customer;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Integer age,
        Gender gender
) {

}
