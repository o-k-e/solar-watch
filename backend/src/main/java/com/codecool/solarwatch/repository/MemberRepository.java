package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.Member;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
   Optional<Member> findByUsername(String username);
    Boolean existsByUsername(String username);
}
