package com.xxby.web;

public record ErrorResponse(int status, String error, String message) {
    
}
