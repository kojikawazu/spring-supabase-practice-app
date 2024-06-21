package com.example.app.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * カスタムアクセス拒否ハンドラー
 * 403エラー発生時にログインページにリダイレクトする
 * @since 2024/06/20
 * @author koji kawazu
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		System.out.println("Access Denied Handler called");
		response.sendRedirect("/login?error=403");	
	}
}
