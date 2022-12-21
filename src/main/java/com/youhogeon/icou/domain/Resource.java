package com.youhogeon.icou.domain;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class Resource extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(length = 16, nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Lob
    @Column(nullable = false)
    private String data;

    @OneToMany(mappedBy = "resource")
    private List<Comment> comments;

    private Timestamp expiredAt;

}
