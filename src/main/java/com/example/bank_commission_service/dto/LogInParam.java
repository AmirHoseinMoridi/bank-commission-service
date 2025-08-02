package com.example.bank_commission_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogInParam {

    @NotBlank(message = "username is not valid !")
    String username;

    @NotBlank(message = "password is not valid !")
    String password;
}
