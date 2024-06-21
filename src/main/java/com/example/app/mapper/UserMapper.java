package com.example.app.mapper;

import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.annotations.*;

import com.example.app.handler.RoleSetTypeHandler;
import com.example.app.handler.UUIDTypeHandler;
import com.example.app.model.Role;
import com.example.app.model.User;

/**
 * ユーザーマッパー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Mapper
public interface UserMapper {
	@Select("SELECT u.id AS user_id, u.username, u.password, u.email, " +
	        "STRING_AGG(r.name, ',') AS role_names " +
	        "FROM users u " +
	        "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
	        "LEFT JOIN roles r ON ur.role_id = r.id " +
	        "WHERE u.username = #{username} " +
	        "GROUP BY u.id")
	@Results({
	    @Result(property = "id", column = "user_id", typeHandler = UUIDTypeHandler.class),
	    @Result(property = "roles", column = "role_names", javaType = Set.class, typeHandler = RoleSetTypeHandler.class)
	})
	User selectByUsername(@Param("username") String username);

    @Select("SELECT r.id, r.name FROM roles r " +
            "JOIN user_roles ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId}")
    @Results({
        @Result(property = "id", column = "id", typeHandler = UUIDTypeHandler.class),
        @Result(property = "name", column = "name")
    })
    Set<Role> selectRolesByUserId(UUID userId);

    @Insert({
        "INSERT INTO users (id, username, password, email)",
        "VALUES (#{id, typeHandler=com.example.app.handler.UUIDTypeHandler}, #{username}, #{password}, #{email})"
    })
    void insertUser(User user);
}
