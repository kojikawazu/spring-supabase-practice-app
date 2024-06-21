package com.example.app.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ロールモデル
 * @since 2024/06/21
 * @author koji kawazu
 */
@Data              
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private UUID id;
    private String name;
}
