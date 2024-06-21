package com.example.app.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

import com.example.app.handler.UUIDTypeHandler;
import com.example.app.model.ToDo;

/**
 * Todoマッパー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Mapper
public interface ToDoMapper {
	@Insert({
	    "INSERT INTO todos (user_id, description, completed) ",
	    "VALUES (#{userId, typeHandler=com.example.app.handler.UUIDTypeHandler}, ",
	    "#{description}, ",
	    "#{completed})"
	})
	void insertToDo(ToDo todo);
    
	@Select({
	    "SELECT id, user_id, description, completed, created_at, updated_at ",
	    "FROM todos ",
	    "WHERE user_id = #{userId, typeHandler=com.example.app.handler.UUIDTypeHandler} ",
	    "ORDER BY id ASC"
	})
	@Results(id = "ToDoResultMap", value = {
    	@Result(property = "id", column = "id", typeHandler = UUIDTypeHandler.class),
        @Result(property = "userId", column = "user_id", typeHandler = UUIDTypeHandler.class),
        @Result(property = "description", column = "description"),
        @Result(property = "completed", column = "completed"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<ToDo> selectAllToDos(@Param("userId") UUID userId);
    
	@Update({
	    "UPDATE todos ",
	    "SET description = #{description}, completed = #{completed} ",
	    "WHERE id = #{id, typeHandler=com.example.app.handler.UUIDTypeHandler} ",
	    "AND user_id = #{userId, typeHandler=com.example.app.handler.UUIDTypeHandler}"
	})
	void updateToDo(ToDo todo);
    
    @Delete({
    	"DELETE FROM todos ",
    	"WHERE id = #{id, typeHandler=com.example.app.handler.UUIDTypeHandler} ",
    	"AND user_id = #{userId}"
    })
    void deleteToDo(@Param("id") UUID id, @Param("userId") UUID userId);
}
