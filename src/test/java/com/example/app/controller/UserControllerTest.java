package com.example.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.app.config.TestSecurityConfig;
import com.example.app.model.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * ユーザーコントローラー[テスト]
 */
@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BindingResult result;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
    	when(result.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("password", "testpassword123")
                .param("email", "test@example.com")
                .with(csrf().asHeader()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        when(userService.validateUser(anyString(), anyString(), any(HttpSession.class))).thenReturn(true);

        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "testpassword123")
                .with(csrf().asHeader()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/welcome"));
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.validateUser(anyString(), anyString(), any(HttpSession.class))).thenReturn(false);

        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "wrongpassword")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testLogoutUser() throws Exception {
        mockMvc.perform(post("/logout")
        		.with(csrf().asHeader()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    public void testShowWelcomePage() throws Exception {       
        mockMvc.perform(get("/welcome")
        		.with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"));
    }
}
