package com.encom.msuser.exception;

public class TokenExpirationException extends RuntimeException {
    public TokenExpirationException(String message) { super(message); }
}
