package com.practice.batch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerCsvDto {

    private Long id;
    private String name;
    private String email;

}
