package com.msvc.user_service.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    private UUID id;
    private String name;
    private String localization;
    private boolean isOpen;
    private LocalDate creationDate;
    private String information;
}
