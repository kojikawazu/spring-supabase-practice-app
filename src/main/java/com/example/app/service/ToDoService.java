package com.example.app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.mapper.ToDoMapper;
import com.example.app.model.ToDo;

/**
 * ToDoサービス
 * @since 2024/06/20
 * @author koji kawazu
 */
@Service
public class ToDoService {
	private ToDoMapper toDoMapper;
	
	/**
	 * コンストラクタ
	 * @param toDoMapper
	 */
	public ToDoService(
			ToDoMapper toDoMapper) {
		this.toDoMapper = toDoMapper;
	}
	
	/**
	 * ToDo保存
	 * @param todo
	 */
	@Transactional
	public void saveToDo(ToDo todo) {
		this.toDoMapper.insertToDo(todo);
	}
	
	/**
	 * ToDo全取得
	 * @param username
	 * @return ToDoリスト
	 */
	public List<ToDo> selectToDos(UUID userId) {
		return this.toDoMapper.selectAllToDos(userId);
	}
	
	/**
	 * ToDo更新
	 * @param todo
	 */
	@Transactional
	public void updateToDo(ToDo todo) {
		this.toDoMapper.updateToDo(todo);
	}
	
	/**
	 * ToDo削除
	 * @param id
	 * @param username
	 */
	@Transactional
	public void deleteToDo(UUID id, UUID userId) {
		this.toDoMapper.deleteToDo(id, userId);
	}
}
