package ru.mypackage.demoproject.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.demoproject.exceptions.StatementNotFoundException;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;
import ru.mypackage.demoproject.services.StatementService;
import ru.mypackage.demoproject.utils.JWTFilter;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = OperatorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OperatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementService statementService;

    @MockBean
    private JWTFilter jwtFilter;

    @Test
    void shouldHaveCorrectGetOneStatementById() throws Exception {
        int idOfStatement = 1;
        StatementType type = StatementType.SENT;

        when(statementService.findOne(idOfStatement, type))
                .thenReturn(new Statement(
                                1,
                                "user_1",
                                "qwe",
                                StatementType.SENT,
                                new Date()
                        )
                );

        ResultActions response = mockMvc.perform(get("/operator/get?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user_1"));
    }

    @Test
    void shouldHaveCorrectGetOneStatementByIdWhenThrowException() throws Exception {
        when(statementService.findOne(any(Integer.class), any(StatementType.class)))
                .thenThrow(new StatementNotFoundException("Statement is not found!"));

        ResultActions response = mockMvc.perform(get("/operator/get?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

    @Test
    void shouldHaveCorrectGetAllStatement() throws Exception {
        String username = "user_1";
        Integer page = 0;
        Boolean desc = false;
        StatementType type = StatementType.SENT;

        when(statementService.findAllStatementsByUserAndStatementType(username, type.toString(), page, desc))
                .thenReturn(
                        List.of(
                                new Statement(),
                                new Statement(),
                                new Statement()
                        )
                );

        ResultActions response = mockMvc.perform(get("/operator/get/all?username=user_1&page=0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statements.size()").value(3));
    }

    @Test
    void shouldHaveCorrectAcceptStatement() throws Exception {
        int idOfStatement = 1;

        doNothing()
                .when(statementService)
                .acceptSentStatement(idOfStatement);

        ResultActions response = mockMvc.perform(post("/operator/accept?id=1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectAcceptStatementWhenThrowException() throws Exception {
        doThrow(new StatementNotFoundException("Statement is not found!"))
                .when(statementService)
                .acceptSentStatement(any(Integer.class));

        ResultActions response = mockMvc.perform(post("/operator/accept?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

    @Test
    void shouldHaveCorrectRejectStatement() throws Exception {
        int idOfStatement = 1;

        doNothing()
                .when(statementService)
                .rejectSentStatement(idOfStatement);

        ResultActions response = mockMvc.perform(post("/operator/reject?id=1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectRejectStatementWhenThrowException() throws Exception {
        doThrow(new StatementNotFoundException("Statement is not found!"))
                .when(statementService)
                .rejectSentStatement(any(Integer.class));

        ResultActions response = mockMvc.perform(post("/operator/reject?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }
}
