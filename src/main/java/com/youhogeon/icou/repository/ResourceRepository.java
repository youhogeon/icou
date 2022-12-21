package com.youhogeon.icou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youhogeon.icou.domain.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    public boolean existsByToken(String token);

}
