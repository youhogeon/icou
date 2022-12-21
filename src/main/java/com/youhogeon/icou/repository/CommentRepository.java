package com.youhogeon.icou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youhogeon.icou.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
