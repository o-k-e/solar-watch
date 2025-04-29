package com.codecool.solarwatch.model.dto.response;

import java.util.Set;

public record MemberPublicResponse(String username, Set<String> roles) {
}
