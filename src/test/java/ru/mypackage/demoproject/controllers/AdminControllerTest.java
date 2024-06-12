package ru.mypackage.demoproject.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.demoproject.dto.UserDTO;
import ru.mypackage.demoproject.models.Statement;
import ru.mypackage.demoproject.models.enums.StatementType;
import ru.mypackage.demoproject.services.StatementService;
import ru.mypackage.demoproject.services.UserService;
import ru.mypackage.demoproject.utils.JWTFilter;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTFilter jwtFilter;

    @MockBean
    private UserService userService;

    @MockBean
    private StatementService statementService;

    @Test
    void shouldHaveCorrectGetUsers() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(List.of(
                        new UserDTO(1, "user_1"),
                        new UserDTO(2, "user_2"),
                        new UserDTO(3, "user_3")
                ));

        ResultActions response = mockMvc.perform(get("/admin/users"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userDTOList.size()").value(3));
    }

    @Test
    void shouldHaveCorrectGetSentStatements() throws Exception {
        when(
                statementService
                        .findAllStatementsByUserAndStatementType(
                                any(String.class),
                                any(String.class),
                                any(Integer.class),
                                any(Boolean.class))
        )
                .thenReturn(List.of(
                        new Statement(1, "user_1", "qwe_1", StatementType.SENT, new Date()),
                        new Statement(2, "user_1", "qwe_2", StatementType.SENT, new Date()),
                        new Statement(3, "user_1", "qwe_3", StatementType.SENT, new Date())
                ));

        ResultActions response = mockMvc.perform(get("/admin/users/statements?type=SENT&username=user_1&page=0&desc=false"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statements.size()").value(3));
    }

    @Test
    void shouldHaveCorrectSetOperator() throws Exception {
        doNothing()
                .when(userService)
                .setOperator(any(String.class));

        ResultActions response = mockMvc.perform(post("/admin/add/operator?username=user_3"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldHaveCorrectThrowExceptionWhenUserNotFound() throws Exception {
        doThrow(new UsernameNotFoundException("User is not valid"))
                .when(userService)
                .setOperator(any(String.class));

        ResultActions response = mockMvc.perform(post("/admin/add/operator?username=user_4"));

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Username is not valid!"));
    }
}
