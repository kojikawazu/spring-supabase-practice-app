package com.example.app.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.app.common.CommonConstants;
import com.example.app.model.Inquiry;
import com.example.app.model.InquiryWithUser;
import com.example.app.model.User;
import com.example.app.service.InquiryService;

import jakarta.servlet.http.HttpSession;

/**
 * お問い合わせコントローラー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Controller
@SessionAttributes("currentUser")
public class InquiryController {
	@Autowired
	private InquiryService inquiryService;
	
	private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);
	
	/**
	 * お問い合わせフォーム表示
	 * @param model モデル
	 * @return inquiryForm or redirect:/login
	 */
	@GetMapping("/inquiry")
	public String showInquiryForm(
		HttpSession session,
		Model model) {
		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
        if (userId == null) {
        	logger.error("Invalid, non session.");
            return "redirect:/login";
        }
        logger.info("Valid, all OK.");
        logger.debug("userId: {}", userId);
        
        User user = inquiryService.selectUserById(userId);
        logger.debug("user: {}", user);
        model.addAttribute("inquiry", new Inquiry());
        model.addAttribute("user", user);
        return "inquiryForm";
	}
	
	/**
	 * お問い合わせsubmit
	 * @param inquiry
	 * @param result
	 * @param session
	 * @param model
	 * @return inquiryForm or redirect:/login or redirect:/inquirySuccess
	 */
	@PostMapping("/inquiry")
	public String submitInquiry(
			@Valid @ModelAttribute Inquiry inquiry,
			BindingResult result,
			HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			logger.error("Invalid, form error.");
			logger.debug("result: {}", result);
            return "inquiryForm";
        }

		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
        if (userId == null) {
        	logger.error("Invalid, non session.");
            return "redirect:/login";
        }
        logger.info("Valid, all OK.");
        logger.debug("userId: {}", userId);
        
        inquiry.setUserId(userId);
        logger.debug("inquiry: {}", inquiry);
        inquiryService.insertInquiry(inquiry);
        return "redirect:/inquirySuccess";
	}

	/**
	 * お問い合わせ成功
	 * @return inquirySuccess
	 */
	@GetMapping("/inquirySuccess")
	public String showInquirySuccess() {
		return "inquirySuccess";
	}
	
	/**
	 * 問い合わせ一覧画面
	 * @param model
	 * @return inquiryList
	 */
	@GetMapping("/admin/inquiries")
	public String showInquiries(Model model) {
		List<InquiryWithUser> inquiriesWithUser = inquiryService.selectAllInquiriesWithUser();
		logger.debug("inquiriesWithUser: {}", inquiriesWithUser);
	    model.addAttribute("inquiries", inquiriesWithUser);
	    return "inquiryList";
	}
}
