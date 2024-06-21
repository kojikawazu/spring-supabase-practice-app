package com.example.app.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * お問い合わせモデル
 * @since 2024/06/20
 * @author koji kawazu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {
	private UUID id;
	private UUID userId;
	private LocalDateTime createdAt;
	
	@NotEmpty
	private String message;
}
