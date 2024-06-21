package com.example.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー結合したお問い合わせモデル
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryWithUser {
	private Inquiry inquiry;
	private User    user;
}
