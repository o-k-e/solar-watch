package com.codecool.solarwatch.model.dto.response;

import java.util.Set;

public record MemberResponse(String username, String password, Set<String> roles) {
}
