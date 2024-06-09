package ru.mypackage.demoproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.demoproject.dto.CreateStatementDTO;
import ru.mypackage.demoproject.dto.RefactorStatementDTO;
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

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatementService statementService;

    @MockBean
    private JWTFilter jwtFilter;

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectGetOneById() throws Exception {
        int idOfStatement = 1;
        String username = "user_1";

        when(statementService.findOne(idOfStatement, username))
                .thenReturn(
                        new Statement(
                                1,
                                "user_1",
                                "qwe",
                                StatementType.SENT,
                                new Date()
                        )
                );

        ResultActions response = mockMvc.perform(get("/user/get?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statement").value("qwe"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectGetOneByIdWhenThrowException() throws Exception {
        when(statementService.findOne(any(Integer.class), any(String.class)))
                .thenThrow(new StatementNotFoundException("Statement is not found!"));

        ResultActions response = mockMvc.perform(get("/user/get?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectGetAllStatements() throws Exception {
        String username = "user_1";
        String type = "SENT";
        Integer page = 0;
        Boolean desc = false;

        when(statementService.findAllStatementsByUserAndStatementType(username, type, page, desc))
                .thenReturn(
                        List.of(
                                new Statement(),
                                new Statement(),
                                new Statement()
                        )
                );

        ResultActions response = mockMvc.perform(get("/user/get/all?type=SENT&page=0"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statements.size()").value(3));
    }

    @Test
    void shouldHaveCorrectCheckStatusOfStatement() throws Exception {
        int idOfStatement = 1;

        when(statementService.checkStatus(idOfStatement))
                .thenReturn(StatementType.ACCEPT);

        ResultActions response = mockMvc.perform(get("/user/check/status?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldHaveCorrectCheckStatusOfStatementWhenThrowException() throws Exception {
        when(statementService.checkStatus(any(Integer.class)))
                .thenThrow(new StatementNotFoundException("Statement is not found!"));

        ResultActions response = mockMvc.perform(get("/user/check/status?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectCreateSentStatement() throws Exception {
        String username = "user_1";
        StatementType type = StatementType.SENT;
        CreateStatementDTO createStatementDTO = new CreateStatementDTO("qwe");

        doNothing()
                .when(statementService)
                .create(username, type, createStatementDTO.getStatement());

        ResultActions response = mockMvc.perform(post("/user/create/sent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStatementDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user_1", roles = "USER")
    void shouldHaveCorrectCreateDraftStatement() throws Exception {
        String username = "user_1";
        StatementType type = StatementType.DRAFT;
        CreateStatementDTO createStatementDTO = new CreateStatementDTO("qwe");

        doNothing()
                .when(statementService)
                .create(username, type, createStatementDTO.getStatement());

        ResultActions response = mockMvc.perform(post("/user/create/draft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStatementDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectSentDraft() throws Exception {
        int idOfStatement = 1;

        doNothing()
                .when(statementService)
                .sentStatementFromDrafts(idOfStatement);

        ResultActions response = mockMvc.perform(post("/user/sent?id=1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectSentDraftWhenThrowException() throws Exception {
        doThrow(new StatementNotFoundException("Statement is not found!"))
                .when(statementService)
                .sentStatementFromDrafts(any(Integer.class));

        ResultActions response = mockMvc.perform(post("/user/sent?id=1"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

    @Test
    void shouldHaveCorrectRefactorDraft() throws Exception {
        RefactorStatementDTO refactorStatementDTO = new RefactorStatementDTO(1, "qwe");

        doNothing()
                .when(statementService)
                .refactor(refactorStatementDTO);

        ResultActions response = mockMvc.perform(post("/user/refactor/draft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refactorStatementDTO))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectRefactorDraftWhenThrowException() throws Exception {
        RefactorStatementDTO refactorStatementDTO = new RefactorStatementDTO(1, "qwe");

        doThrow(new StatementNotFoundException("Statement is not found!"))
                .when(statementService)
                .refactor(any(RefactorStatementDTO.class));

        ResultActions response = mockMvc.perform(post("/user/refactor/draft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refactorStatementDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Statement with this 'id' wasn't found!"));
    }

}
