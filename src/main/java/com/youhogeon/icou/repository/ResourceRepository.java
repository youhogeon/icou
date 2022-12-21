package com.youhogeon.icou.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youhogeon.icou.domain.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    public boolean existsByToken(String token);
    public Optional<Resource> findByToken(String token);

}
