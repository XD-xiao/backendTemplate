// com.example.springboottemplate.Exception.TokenInvalidException.java
package com.example.backendtemplate.Exception;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String message) {
        super(message);
    }
}