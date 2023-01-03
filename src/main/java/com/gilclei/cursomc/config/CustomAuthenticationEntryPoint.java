package com.gilclei.cursomc.config;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.gilclei.cursomc.resources.exceptions.StandardError;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	 @Override
	    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
		 	String error = "Forbidden";
			HttpStatus status = HttpStatus.FORBIDDEN;
		 	res.setContentType("application/json;charset=UTF-8");
	        res.setStatus(status.value());	        
	        StandardError err = new StandardError(Instant.now(), status.value(), error, "Access Denied",
					req.getRequestURI());
	        res.getWriter().write(err.toJson());
	    }

}
