package com.example.app.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.app.common.CommonConstants;
import com.example.app.mapper.UserMapper;
import com.example.app.model.Role;
import com.example.app.model.User;

import jakarta.servlet.http.HttpSession;

public class UserServiceTest {

	@Mock
	private UserMapper userMapper;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private HttpSession session;
	
	@InjectMocks
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		UUID userId = UUID.randomUUID();
		Set<Role> roles = new HashSet<>();
        roles.add(new Role(UUID.randomUUID(), CommonConstants.NORMAL_ROLE_NAME));
		user = new User(userId, "testuser", "testpassword123", "test@example.com", null, null, roles);
	}
	
	@Test
	public void testFindByUsername() {
		when(userMapper.selectByUsername("testuser")).thenReturn(user);
		User foundUser = userService.selectByUsername("testuser");
		assertNotNull(foundUser);
		assertEquals("testuser", foundUser.getUsername());
	}
	
	@Test
	public void testRegisterUser() {
		when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
		userService.register(user);
		assertEquals("encodedPassword", user.getPassword());
		verify(userMapper, times(1)).insertUser(user);
	}
	
	@Test
	public void testValidateUserSuccess() {
		when(userMapper.selectByUsername("testuser")).thenReturn(user);
		when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);
		
		boolean isValid = userService.validateUser("testuser", "wrongpassword", session);
		assertFalse(isValid);
		verify(session, times(0)).setAttribute(anyString(), any());
	}
	
	@Test
	public void testAutoLogin() {
		userService.autoLogin(user, "testpassword123", session);
		verify(session,times(1)).setAttribute(anyString(), any());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertNotNull(authentication);
		assertEquals(user.getUsername(), authentication.getName());
	}
}
