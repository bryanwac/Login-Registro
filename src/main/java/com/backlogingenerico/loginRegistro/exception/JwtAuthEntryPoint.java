package com.backlogingenerico.loginRegistro.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException, ServletException {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatus(401);
        response.setStatus(errorDetails.getStatus());
        errorDetails.setTimestamp(new Date());
        errorDetails.setException(e);
        errorDetails.setDetails(e.getMessage());
        errorDetails.setMessage("O documento ou senha informados estão incorretos!");

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, errorDetails);
        out.flush();


//        logger.error("Unauthorized error. Message - {O CPF ou senha informados estão incorretos!}", e.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "O CPF ou senha informados estão incorretos!");

    }
}
