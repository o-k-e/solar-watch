package com.codecool.solarwatch.model.dto.response;

import lombok.Getter;

@Getter
public class SuccessResponse {
    private boolean success;

    public SuccessResponse(boolean success) {
        this.success = success;
    }

}
