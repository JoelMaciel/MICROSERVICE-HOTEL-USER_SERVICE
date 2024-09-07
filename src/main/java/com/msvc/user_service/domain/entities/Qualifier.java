package com.msvc.user_service.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Qualifier {

    private String qualifierId;
    private String userId;
    private String hotelId;
    private int qualification;
    private LocalDate qualificationDate;
    private String observation;
}
