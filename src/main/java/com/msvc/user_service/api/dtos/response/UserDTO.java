package com.msvc.user_service.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.msvc.user_service.domain.entities.Qualifier;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String information;
    private LocalDate creationDate;
    private List<Qualifier> qualifier;
}
