package com.example.app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.mapper.InquiryMapper;
import com.example.app.model.Inquiry;
import com.example.app.model.InquiryWithUser;
import com.example.app.model.User;

/**
 * お問い合わせサービス
 * @since 2024/06/20
 * @author koji kawazu
 */
@Service
public class InquiryService {
	private InquiryMapper inquiryMapper;
	
	/**
	 * コンストラクタ
	 * @param inquiryMapper
	 */
	public InquiryService(InquiryMapper inquiryMapper) {
		this.inquiryMapper = inquiryMapper;
	}

	/**
	 * お問い合わせ追加
	 * @param inquiry
	 */
	@Transactional
	public void insertInquiry(Inquiry inquiry) {
		this.inquiryMapper.insertInquiry(inquiry);
    }
	
	/**
	 * ユーザーIDからユーザー取得
	 * @param userId
	 * @return User
	 */
    public User selectUserById(UUID userId) {
        return this.inquiryMapper.selectUserById(userId);
    }
    
    /**
     * ユーザー付き全お問い合わせリスト取得
     * @return List<InquiryWithUser>
     */
    public List<InquiryWithUser> selectAllInquiriesWithUser() {
        return this.inquiryMapper.selectAllInquiriesWithUser();
    }
}
