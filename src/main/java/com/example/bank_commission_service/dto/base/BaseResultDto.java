package com.example.bank_commission_service.dto.base;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResultDto {
    String errCode;
    String errMsg;
}
