package com.example.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.app.model.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * ユーザーコントローラー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Controller
@SessionAttributes("currentUser")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	/**
	 * 登録フォーム
	 * @param model モデル
	 * @return register
	 */
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	/**
	 * ユーザー登録
	 * @param user ユーザー
	 * @param result 結果
	 * @param model モデル
	 * @return register or redirect:/login
	 */
	@PostMapping("/register")
	public String registerUser(
			@Valid @ModelAttribute User user,
			BindingResult result,
			Model model) {
		logger.info("Received POST request for user registration");

	    if (result.hasErrors()) {
	        logger.error("Invalid, registerUser");
	        return "register";
	    }
	    
	    logger.info("valid, registerUser");
	    userService.register(user);
	    return "redirect:/login";
	}
	
	/**
	 * ログインユーザーフォーム
	 * @param session
	 * @param model
	 * @return login
	 */
	@GetMapping("/login")
	public String showLoginForm(
			HttpSession session,
			Model model) {
		return "login";
	}
	
	/**
	 * ログインユーザー
	 * @param username ユーザー名
	 * @param password パスワード
	 * @param session セッション
	 * @param model モデル
	 * @return login or redirect:/welcome
	 */
	@PostMapping("/login")
	public String loginUser(
	        @RequestParam String username,
	        @RequestParam String password,
	        HttpSession session,
	        Model model) {
	    logger.info("Received POST request for user loginUser");

	    boolean isValidUser = userService.validateUser(username, password, session);
	    logger.info("User validation result: " + isValidUser);

	    if (isValidUser) {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Authenticated user: " + auth.getName());
            
            User user = userService.selectByUsername(username);
            session.setAttribute("currentUser", user.getId());
            session.removeAttribute("csrfToken");
            
            // リダイレクト前に認証情報をログ出力this.printAuthStatus();
            this.printAuthStatus();
            return "redirect:/welcome";    
	    } else {
	        logger.error("Invalid username or password");
	        model.addAttribute("error", "Invalid username or password");
	        return "login";
	    }
	}
	
	/**
	 * ログアウト
	 * @param sessionStatus　セッション状態
	 * @return redirect:/login
	 */
	@GetMapping("/logout")
	public String logoutUser(SessionStatus sessionStatus, HttpSession session) {
		// Spring Security のコンテキストをクリア
	    SecurityContextHolder.clearContext();
	    // HTTP セッションを無効化
	    session.invalidate();
	    // セッションの状態を完了
		sessionStatus.setComplete();
		
		this.printAuthStatus();
		return "redirect:/login";
	}
	
	/**
	 * Welcomeページの表示
	 * @return welcome
	 */
	@GetMapping("/welcome")
	public String showWelcomePage() {
		this.printAuthStatus();
		return "welcome";
	}
	
	/**
	 * 認証状態のprint出力
	 */
	private void printAuthStatus() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Accessing user: " + auth.getName());
        logger.debug("Authorities: "   + auth.getAuthorities());
	}
}
