package com.codecool.solarwatch.service.mapper;

import com.codecool.solarwatch.model.dto.response.MemberPublicResponse;
import com.codecool.solarwatch.model.entity.Member;
import com.codecool.solarwatch.model.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MemberMapper {

    public MemberPublicResponse mapToMemberPublicResponse(Member member) {
        return new MemberPublicResponse(
                member.getUsername(),
                mapRoles(member.getRoles())
        );
    }

    private Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}