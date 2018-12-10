package com.itsight.component;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandlerHttpMultipart extends ResponseEntityExceptionHandler  {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public @ResponseBody String handleErrorByMaxUploadSizeExceededException(MultipartException e) {
        return "-99";
    }
	
	@ExceptionHandler(MultipartException.class)
    public @ResponseBody String handleErrorByFileSizeLimitExceededException(MultipartException e) {
        return "-99";
    }

}
