package com.example.bank_commission_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterParam {

    @NotBlank()
    String username;

    @Email()
    String email;

    @NotBlank()
    String password;

    @NotBlank()
    String firstName;

    @NotBlank()
    String lastName;

}
