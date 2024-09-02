package com.msvc.user_service.api.dtos.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String information;
    private LocalDate creationDate;
}
