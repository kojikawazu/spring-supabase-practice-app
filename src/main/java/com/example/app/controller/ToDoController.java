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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.app.common.CommonConstants;
import com.example.app.model.ToDo;
import com.example.app.service.ToDoService;

import jakarta.servlet.http.HttpSession;

/**
 * ToDoコントローラー
 * @since 2024/06/20
 * @author koji kawazu
 */
@Controller
@SessionAttributes(CommonConstants.SESSION_CURRENT_USER_PROP)
public class ToDoController {
	@Autowired
	private ToDoService toDoService;
	
	private static final Logger logger = LoggerFactory.getLogger(ToDoController.class);
	
	/**
	 * ToDoリスト表示
	 * @param model
	 * @param session
	 * @return todoList
	 */
	@GetMapping("/todos")
	public String showToDoList(
			Model model,
			HttpSession session) {
		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
		logger.debug("userId : {}", userId);
		
		List<ToDo> toDos = toDoService.selectToDos(userId);
		logger.debug("toDos: {}", toDos);
		model.addAttribute("todos", toDos);
		model.addAttribute("newToDo", new ToDo());
		
		return "todoList";
	}
	
	/**
	 * ToDo追加
	 * @param toDo
	 * @param result
	 * @param session
	 * @param model
	 * @return todoList or redirect:/todos
	 */
	@PostMapping("/todos")
	public String addToDo(
			@Valid @ModelAttribute("newToDo") ToDo newToDo,
			BindingResult result,
			HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			logger.error("Invalid, validate error");
			logger.debug("result: {}", result);
			UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
			List<ToDo> toDos = toDoService.selectToDos(userId);
			logger.debug("userId: {}", userId);
			logger.debug("toDos: {}", toDos);
			model.addAttribute("todos", toDos);
			model.addAttribute("newToDo", newToDo);
			return "todoList";
		}
		logger.error("Valid, addToDo");
		logger.debug("newToDo: {}", newToDo);
		
		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
		logger.debug("userId: {}", userId);
		newToDo.setUserId(userId);
		newToDo.setCompleted(false);
		toDoService.saveToDo(newToDo);
		return "redirect:/todos";
	}
	
	/**
	 * ToDo更新
	 * @param toDo
	 * @param session
	 * @return redirect:/todos
	 */
	@PostMapping("/todos/update")
	public String updateToDo(
			@Valid @ModelAttribute("todo") ToDo toDo,
			BindingResult result,
			HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			logger.error("Invalid, updateToDo");
			logger.debug("result: {}", result);
			UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
	        List<ToDo> toDos = toDoService.selectToDos(userId);
	        logger.debug("userId: {}", userId);
			logger.debug("toDos: {}", toDos);
	        model.addAttribute("todos", toDos);
	        return "todoList";
	    }
		logger.info("Valid, updateToDo");
		logger.debug("toDo: {}", toDo);

		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
	    logger.debug("userId: {}", userId);
	    
	    try {
	    	if (toDo.getCompleted() == null) {
	    		toDo.setCompleted(false);
	    	}
	    	
	        toDoService.updateToDo(toDo);
	    } catch (Exception e) {
	        logger.error("Error updating ToDo", e);
	        model.addAttribute("errorMessage", "Error updating ToDo: " + e.getMessage());
	        userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
	        List<ToDo> toDos = toDoService.selectToDos(userId);
	        logger.debug("userId: {}", userId);
	        logger.debug("toDos: {}", toDos);
	        model.addAttribute("todos", toDos);
	        return "todoList";
	    }
	    
	    return "redirect:/todos";
	}
	
	/**
	 * ToDo削除
	 * @param id
	 * @param session
	 * @return　redirect:/todos
	 */
	@PostMapping("/todos/delete")
	public String deleteToDo(
			@RequestParam UUID id,
			HttpSession session) {
		UUID userId = (UUID)session.getAttribute(CommonConstants.SESSION_CURRENT_USER_PROP);
		logger.debug("userId: {}", userId);
		toDoService.deleteToDo(id, userId);
		return "redirect:/todos";
	}
}
