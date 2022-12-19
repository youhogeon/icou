package com.youhogeon.icou.Model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @CreatedDate
    private Timestamp created_at;

    @LastModifiedDate
    private Timestamp updated_at;

}
