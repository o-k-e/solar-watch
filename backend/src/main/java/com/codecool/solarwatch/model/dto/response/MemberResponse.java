package com.codecool.solarwatch.model.dto.response;

import com.codecool.solarwatch.model.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class MemberResponse {
    private String username;
    private String password;
    private Set<String> roles;
}
