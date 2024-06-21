package com.example.app.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザーモデル
 * @since 2024/06/20
 * @author koji kawazu
 */
@Data               // Lombok setterやgetterを自動生成する
@NoArgsConstructor  // デフォルトコンストラクタ
@AllArgsConstructor // 全フィールドに対する初期化値を引数にとるコンストラクタ
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	private UUID id;
	
	@NotEmpty
	@Size(min = 3, max = 50)
	private String username;
	
	@NotEmpty
	@Size(min = 8, max = 100)
	private String password;
	
	@NotEmpty
	@Email
	private String email;
	
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
