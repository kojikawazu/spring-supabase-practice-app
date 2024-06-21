package com.example.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import com.example.app.common.CommonConstants;

/**
 * ユーザー[テスト]
 * @since 2024/06/20
 * @author koji kawazu
 */
public class UserTest {
	
	private User user;
	private Validator validator;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		UUID userId = UUID.randomUUID();
		Set<Role> roles = new HashSet<>();
        roles.add(new Role(UUID.randomUUID(), CommonConstants.NORMAL_ROLE_NAME));
		user = new User(userId, "testuser", "testpassword123", "test@example.com", null, null, roles);
	}
	
	@Test
	public void testGetUsername() {
		assertEquals("testuser", user.getUsername());
	}
	
	@Test
	public void testSetUsername() {
		user.setUsername("newusername");
		assertEquals("newusername", user.getUsername());
	}
	
	@Test
	public void testGetPassword() {
		assertEquals("testpassword123", user.getPassword());
	}
	
	@Test
	public void testSetPassword() {
		user.setPassword("newpassword123");
		assertEquals("newpassword123", user.getPassword());
	}

	@Test
	public void testGetEmail() {
		assertEquals("test@example.com", user.getEmail());
	}
	
	@Test
	public void testSetEmail() {
		user.setEmail("new@example.com");
		assertEquals("new@example.com", user.getEmail());
	}
	
	@Test
	public void testGetAuthorities() {
		assertTrue(user
			.getAuthorities()
			.stream()
			.anyMatch(grantedAuthority -> 
				grantedAuthority.getAuthority().equals("ROLE_USER")));
	}
	
	@Test
	public void testIsAccountNonExpired() {
		assertTrue(user.isAccountNonExpired());
	}
	
	@Test
	public void testIsAccountNonLocked() {
		assertTrue(user.isAccountNonLocked());
	}
	
	@Test
	public void testIsCredentialsNonExpired() {
		assertTrue(user.isCredentialsNonExpired());
	}
	
	@Test
	public void testIsEnabled() {
		assertTrue(user.isEnabled());
	}
	
	@Test
	public void testInvalidUserNameTooShort() {
		user.setUsername("ab");
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}
	
	@Test
	public void testInvalidUserNameTooLong() {
		user.setUsername("a".repeat(51));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}
	
	@Test
	public void testInvalidPasswordTooShort() {
		user.setPassword("short");
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}
	
	@Test
	public void testInvalidPassowrdTooLong() {
		user.setPassword("a".repeat(101));
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}
	
	@Test
	public void testInvalidEmail() {
		user.setEmail("invalid-email");
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}	
}
