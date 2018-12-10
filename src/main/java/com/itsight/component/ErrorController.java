package com.itsight.component;

import com.itsight.constants.ViewConstant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.View;

@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return ViewConstant.ERROR404;
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return ViewConstant.ERROR500;
            }
        }
        return ViewConstant.ERROR500;
    }
}
