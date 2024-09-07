package com.msvc.user_service.api.dtos.response;

import com.msvc.user_service.domain.entities.Qualifier;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private List<Qualifier> qualifier;
}
