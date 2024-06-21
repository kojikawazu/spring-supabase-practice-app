package com.example.app.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Todoモデル
 * @since 2024/06/20
 * @author koji kawazu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {
	private UUID id;
	private UUID userId;
	private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	
	@NotEmpty
	@Size(min = 1, max = 255)
	private String description;
	
}
