package com.codecool.solarwatch.model.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class MemberPublicResponse {

    private String username;
    private Set<String> roles;
}
