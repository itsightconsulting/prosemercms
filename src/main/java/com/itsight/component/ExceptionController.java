package com.itsight.component;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(NumberFormatException.class)
	public @ResponseBody String handlerNumberFormatoExcception(HttpServletRequest request, NumberFormatException e, Model model) {
      return "-88";
	}
}
