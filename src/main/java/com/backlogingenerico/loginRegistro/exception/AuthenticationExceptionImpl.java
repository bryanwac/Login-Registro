package com.backlogingenerico.loginRegistro.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class AuthenticationExceptionImpl extends BadCredentialsException {
    public AuthenticationExceptionImpl(String msg) {
        super(msg);
    }

    public AuthenticationExceptionImpl(String msg, Throwable cause) {
        super(msg, cause);
    }
}
