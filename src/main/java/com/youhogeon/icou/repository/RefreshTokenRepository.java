package com.youhogeon.icou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youhogeon.icou.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKey(Long key);

}