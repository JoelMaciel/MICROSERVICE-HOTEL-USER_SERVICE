package com.msvc.user_service.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {

    @Size(min = 8, max = 30)
    private String name;

    @Email
    @NotBlank
    private String email;

    @Size(min = 15, max = 200)
    private String information;
}
