package com.codecool.solarwatch.model.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    public ErrorResponse(String message) {
        this.message = message;
    }
}
