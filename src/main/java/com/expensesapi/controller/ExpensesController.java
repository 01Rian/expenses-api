package com.expensesapi.controller;

import com.expensesapi.domain.Expense;
import com.expensesapi.helper.NullAwareBeanUtilsBean;
import com.expensesapi.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class ExpensesController {

    private ExpenseRepository expenseRepository;
    private NullAwareBeanUtilsBean beanUtilsBean;


    public ExpensesController(ExpenseRepository expenseRepository, NullAwareBeanUtilsBean beanUtilsBean) {
        this.expenseRepository = expenseRepository;
        this.beanUtilsBean = beanUtilsBean;
    }

    @GetMapping("/expenses")
    public List<EntityModel<Expense>> findAll() {
        List<EntityModel<Expense>> expenses = StreamSupport.stream(expenseRepository.findAll().spliterator(), false)
                .map(ex -> EntityModel.of(ex,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findById(ex.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findAll()).withRel("expenses")))
                .collect(Collectors.toList());

        return expenses;
    }

    @GetMapping("/expenses/{id}")
    public EntityModel<Expense> findById(@PathVariable("id") Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(expense,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findAll()).withRel("expenses"));
    }

    @PostMapping("/expenses")
    public ResponseEntity<EntityModel<Expense>> save(@Valid @RequestBody Expense expense) {
        Expense savedExpense = expenseRepository.save(expense);

        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(savedExpense,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findById(savedExpense.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findAll()).withRel("expenses")));
    }

    @PutMapping("/expenses/{id}")
    public EntityModel<Expense> update(@Valid @RequestBody Expense expense, @PathVariable("id") Long id) {
        findById(id);
        expense.setId(id);
        Expense updatedExpense = expenseRepository.save(expense);

        return EntityModel.of(updatedExpense,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findById(updatedExpense.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findAll()).withRel("expenses"));
    }

    @PatchMapping("/expenses/{id}")
    public EntityModel<Expense> patchUpdate(@RequestBody Expense expense, @PathVariable("id") Long id) throws InvocationTargetException, IllegalAccessException {
        Expense existingExpense = findById(id).getContent();

        try {
            beanUtilsBean.copyProperties(existingExpense, expense);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while updating expense.", e);
        }

        Expense patchedExpense = expenseRepository.save(existingExpense);

        return EntityModel.of(patchedExpense,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findById(patchedExpense.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ExpensesController.class).findAll()).withRel("expenses"));
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        findById(id);
        expenseRepository.deleteById(id);

        return ResponseEntity.ok()
                .build();
    }
}
