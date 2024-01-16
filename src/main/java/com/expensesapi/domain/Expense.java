package com.expensesapi.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class Expense {

    @Id
    private Long id;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDate date;
    @NotNull
    private Double value;
    @NotNull
    private Category category;
}
