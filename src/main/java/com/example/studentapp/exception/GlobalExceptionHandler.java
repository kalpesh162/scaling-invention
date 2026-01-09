package com.example.studentapp.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
		logger.error("IllegalArgumentException: {}", ex.getMessage());
		model.addAttribute("error", "Not Found");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("status", 404);
		return "error";
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(NoHandlerFoundException ex, Model model) {
		logger.error("Page not found: {}", ex.getRequestURL());
		model.addAttribute("error", "Page Not Found");
		model.addAttribute("message", "The page you're looking for doesn't exist.");
		model.addAttribute("status", 404);
		return "error";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGeneralException(Exception ex, Model model) {
		logger.error("Unexpected error occurred", ex);
		model.addAttribute("error", "Internal Server Error");
		model.addAttribute("message", "Something went wrong. Please try again later.");
		model.addAttribute("status", 500);
		return "error";
	}
}