package com.expensesapi;

import com.expensesapi.controller.ExpensesController;
import com.expensesapi.domain.Category;
import com.expensesapi.domain.Expense;
import com.expensesapi.helper.NullAwareBeanUtilsBean;
import com.expensesapi.repository.ExpenseRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExpensesControllerTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private NullAwareBeanUtilsBean beanUtilsBean;

    @InjectMocks
    private ExpensesController expensesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Mockando o comportamento do repository
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(
                new Expense(1L, "Teste controller findAll 1", LocalDate.now(), 10.0, Category.OTHER),
                new Expense(2L, "Teste controller findAll 2", LocalDate.now(), 20.0, Category.OTHER)
        ));

        // Chamando o método do controller
        List<EntityModel<Expense>> result = expensesController.findAll();

        // Verificando o resultado
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(new Expense(1L, "Teste controller findById", LocalDate.now(), 10.0, Category.OTHER)));

        EntityModel<Expense> result = expensesController.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testSave() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(new Expense(1L, "Teste controller save", LocalDate.now(), 10.0, Category.OTHER));

        ResponseEntity<EntityModel<Expense>> result = expensesController.save(new Expense());

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void testUpdate() {
        // Mockando o comportamento do repository para o método findById
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(new Expense(1L, "Teste controller update", LocalDate.now(), 10.0, Category.OTHER)));

        // Mockando o comportamento do repository para o método save
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense updatedExpense = invocation.getArgument(0);
            updatedExpense.setValue(20.0); // Adjust as needed
            updatedExpense.setCategory(Category.FOOD); // Adjust as needed
            return updatedExpense;
        });

        // Chamando o método do controller
        EntityModel<Expense> result = expensesController.update(new Expense(), 1L);

        // Verificando o resultado
        assertNotNull(result);
        assertEquals(Category.FOOD, result.getContent().getCategory());
        assertEquals(20.0, result.getContent().getValue());

        // Verificando se o método findById foi chamado com os parâmetros corretos
        verify(expenseRepository, times(1)).findById(1L);

        // Verificando se o método save foi chamado com os parâmetros corretos
        verify(expenseRepository, times(1)).save(any(Expense.class));
        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseCaptor.capture());
        Expense savedExpense = expenseCaptor.getValue();
        assertNotNull(savedExpense);
    }

    @Test
    void testPatchUpdate() throws InvocationTargetException, IllegalAccessException {

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(new Expense(1L, "Teste controller pacth antigo", LocalDate.now(), 10.0, Category.OTHER)));
        when(expenseRepository.save(any(Expense.class))).thenReturn(new Expense(1L, "Teste controller pacth Atualizada", LocalDate.now(), 20.0, Category.FOOD));

        EntityModel<Expense> result = expensesController.patchUpdate(new Expense(), 1L);

        assertNotNull(result);
        assertEquals("Teste controller pacth Atualizada", result.getContent().getDescription());
        assertEquals(20.0, result.getContent().getValue());
        assertEquals(Category.FOOD, result.getContent().getCategory());
    }

    @Test
    void testDelete() {

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(new Expense(1L, "Teste controller delete", LocalDate.now(), 15.0, Category.HEALTH)));

        ResponseEntity<Void> result = expensesController.delete(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
