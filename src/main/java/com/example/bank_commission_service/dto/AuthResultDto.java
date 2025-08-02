package com.example.bank_commission_service.dto;

import com.example.bank_commission_service.dto.base.BaseResultDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResultDto extends BaseResultDto {
    String token;

    public AuthResultDto(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public AuthResultDto(String errCode, String errMsg, String token) {
        super(errCode, errMsg);
        this.token = token;
    }
}
