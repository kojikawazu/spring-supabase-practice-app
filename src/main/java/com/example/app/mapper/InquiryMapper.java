package com.example.app.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.app.handler.UUIDTypeHandler;
import com.example.app.model.Inquiry;
import com.example.app.model.InquiryWithUser;
import com.example.app.model.User;

/**
 * お問い合わせマッパー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Mapper
public interface InquiryMapper {
	
	@Insert({
		"INSERT INTO inquiries (user_id, message) ", 
		"VALUES (#{userId, typeHandler=com.example.app.handler.UUIDTypeHandler}, ", 
		"#{message})"
	})
	void insertInquiry(Inquiry inquiry);
	
	@Select({
		"SELECT * ",
		"FROM users ",
		"WHERE id = #{userId, typeHandler=com.example.app.handler.UUIDTypeHandler}"
	})
    User selectUserById(@Param("userId") UUID userId);
	
	@Select({
		"SELECT i.id as inquiry_id, i.message, i.created_at, u.id as user_id, u.username, u.email ",
        "FROM inquiries i ",
        "JOIN users u ON i.user_id = u.id"
    })
    @Results({
        @Result(column="inquiry_id", property="inquiry.id"),
        @Result(column="user_id", property="user.id", typeHandler=UUIDTypeHandler.class),
        @Result(column="username", property="user.username"),
        @Result(column="email", property="user.email"),
        @Result(column="message", property="inquiry.message"),
        @Result(column="created_at", property="inquiry.createdAt")
    })
    List<InquiryWithUser> selectAllInquiriesWithUser();
}
