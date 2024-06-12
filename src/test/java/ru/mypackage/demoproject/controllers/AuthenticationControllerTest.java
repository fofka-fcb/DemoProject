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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mypackage.demoproject.dto.LoginDTO;
import ru.mypackage.demoproject.dto.responses.LoginResponseDTO;
import ru.mypackage.demoproject.dto.responses.RegisterResponseDTO;
import ru.mypackage.demoproject.dto.RegistrationDTO;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.services.AuthenticationService;
import ru.mypackage.demoproject.utils.JWTFilter;
import ru.mypackage.demoproject.utils.UserValidator;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private JWTFilter jwtFilter;

    @Test
    void shouldHaveCorrectRegisterUser() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO(
                "user_1",
                "password",
                "000");

        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO(
                "user_1",
                "password",
                Set.of(new Role(3, "USER"))
        );

        when(
                authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword(), registrationDTO.getPhone()))
                .thenReturn(registerResponseDTO);

        ResultActions response = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password"));
    }

    @Test
    void shouldNotHaveCorrectLoginUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO(
                "user_1",
                "password"
        );

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                "user_1",
                Set.of(new Role(3, "USER")),
                "qwe"
        );

        when(authenticationService.loginUser(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(loginResponseDTO);

        ResultActions response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO))
        );

        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").value("qwe"));
    }

}
