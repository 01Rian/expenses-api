package com.expensesapi.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
