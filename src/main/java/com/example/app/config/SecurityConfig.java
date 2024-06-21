package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.app.common.CommonConstants;
import com.example.app.handler.CustomAccessDeniedHandler;
import com.example.app.handler.CustomAuthenticationEntryPoint;

/**
 * セキュリティ設定
 * @since 2024/06/20
 * @author koji kawazu
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    
	/**
	 * コンストラクタ
	 * @param accessDeniedHandler　カスタムアクセス拒否ハンドラー
	 * @param authenticationEntryPoint カスタム認証エントリーポイント
	 */
    public SecurityConfig(
    	CustomAccessDeniedHandler accessDeniedHandler, 
    	CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // 認証不要のURLパターンを設定する
            	.requestMatchers("/register", "/login", "/logout", "/css/**", "/js/**", "/images/**").permitAll()
                // ロールUSERが必要なURLパターンを設定
            	.requestMatchers(
            			"/welcome",
            			"/todos/**",
            			"/inquiry/**"
            	).hasRole(CommonConstants.ROLE_MAP.get(CommonConstants.NORMAL_ROLE_NAME))
            	.requestMatchers(
            			"/admin/**"
            	).hasRole(CommonConstants.ROLE_MAP.get(CommonConstants.ADMIN_ROLE_NAME))
            	// その他のURLは認証が必要
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
            	// csrfトークンの設定
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .logout(logout -> logout
            	// ログアウトURLとログアウト成功後のリダイレクトURLを設定	
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .sessionManagement(session -> session
            	// セッションポリシーを設定
            	.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .exceptionHandling(exception -> exception
            	// カスタムアクセス拒否ハンドラーとカスタム認証エントリーポイントを設定
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            ;

        return http.build();
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
