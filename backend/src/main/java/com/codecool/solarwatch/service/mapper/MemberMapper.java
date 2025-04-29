package com.codecool.solarwatch.service.mapper;

import com.codecool.solarwatch.model.dto.response.MemberPublicResponse;
import com.codecool.solarwatch.model.entity.Member;
import com.codecool.solarwatch.model.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class MemberMapper {

    public MemberPublicResponse mapToMemberPublicResponse(Member member) {
        MemberPublicResponse response = new MemberPublicResponse();
        response.setUsername(member.getUsername());
        response.setRoles(mapRoles(member.getRoles()));
        return response;
    }

    private Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
