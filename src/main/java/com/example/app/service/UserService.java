package com.example.app.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.mapper.UserMapper;
import com.example.app.model.User;

import jakarta.servlet.http.HttpSession;

/**
 * ユーザーサービス
 * @since 2024/06/20
 * @author koji kawazu
 */
@Service
public class UserService {
	private UserMapper userMapper;
	private BCryptPasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	/**
	 * コンストラクタ
	 * @param userMapper
	 * @param passwordEncoder
	 */
	public UserService(
			UserMapper userMapper,
			BCryptPasswordEncoder passwordEncoder) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 * ユーザー名からデータ取得
	 * @param username ユーザー名
	 * @return ユーザーデータ
	 */
	public User selectByUsername(String username) {
		return userMapper.selectByUsername(username);
	}
	
	/**
	 * 登録
	 * @param user ユーザー名
	 */
	@Transactional
	public void register(User user) {
		try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setId(UUID.randomUUID());
            logger.debug("Registering user: " + user);
            userMapper.insertUser(user);
            logger.info("User registered successfully");
        } catch (Exception e) {
            logger.error("Error registering user: " + e.getMessage(), e);
            throw e;
        }
	}
	
	/**
	 * ユーザーバリデート
	 * @param username　ユーザー名
	 * @param password パスワード
	 * @param session HTTPセッション
	 * @return true: 認証成功, false: 認証失敗
	 */
	public boolean validateUser(String username, String password, HttpSession session) {
		User user = userMapper.selectByUsername(username);
		if (user == null) {
			logger.error("User not found: {}", username);
	        return false;
		}
		
        boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
        logger.debug("Password match for user {}: {}", username, isPasswordMatch);
        if (isPasswordMatch) {
        	autoLogin(user, password, session);
        }
        return isPasswordMatch;
	}
	
	/**
     * 自動ログイン
     * @param username ユーザー名
     * @param password パスワード
     * @param session HTTPセッション
     */
    public void autoLogin(User user, String password, HttpSession session) {
    	// 認証トークンを作成
    	UsernamePasswordAuthenticationToken authToken = 
    		new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.debug("Created Authentication token.");
        
        // SecurityContext を HttpSession に保存
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        logger.debug("Saved HTTPSession.");
        
        // 認証情報を確認するためにログ出力
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Current authenticated user: " + currentAuth.getName());
        logger.debug("Current authorities: " + currentAuth.getAuthorities());
    }
}
