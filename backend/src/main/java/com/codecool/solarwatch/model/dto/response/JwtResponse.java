package com.codecool.solarwatch.model.dto.response;

import java.util.Set;

public record JwtResponse(String jwt, String userName, Set<String> roles) {
}
