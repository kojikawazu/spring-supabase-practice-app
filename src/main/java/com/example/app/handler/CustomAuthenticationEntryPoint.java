package com.example.app.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * カスタム認証エントリーポイント
 * 未認証ユーザーが保護されたリソースにアクセスしようとした際に
 * ログインページにリダイレクトする
 * @since 2024/06/20
 * @author koji kawazu
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		System.out.println("Custom Authentication Entry Point called");
		response.sendRedirect("/login?error=403");	
	}
}
