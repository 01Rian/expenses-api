package com.expensesapi.repository;

import com.expensesapi.domain.Expense;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
}
