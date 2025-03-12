package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String nickname;

    public AuthUser(Long id, String email, UserRole role, String nickname) {
        this.id = id;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
        this.nickname = nickname;
    }

    public UserRole getUserRole() {
        for (GrantedAuthority authority : authorities) {
            return UserRole.valueOf(authority.getAuthority());
        }
        throw new IllegalArgumentException("권한을 찾을 수 없습니다.");
    }

}
