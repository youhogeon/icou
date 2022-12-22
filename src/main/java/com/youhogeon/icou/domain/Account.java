package com.youhogeon.icou.domain;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.youhogeon.icou.controller.dto.request.AccountCreateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
public class Account extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 16, nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "account")
    private List<Comment> comments;

    @OneToMany(mappedBy = "account")
    private List<Resource> resources;

    @LastModifiedDate
    private Timestamp updatedAt;

    public static Account from(AccountCreateRequestDto accountCreateRequestDto, PasswordEncoder passwordEncoder) {
        return Account.builder()
            .email(accountCreateRequestDto.getEmail())
            .password(passwordEncoder.encode(accountCreateRequestDto.getPassword()))
            .nickname(accountCreateRequestDto.getNickname())
            .build();
    }
}
