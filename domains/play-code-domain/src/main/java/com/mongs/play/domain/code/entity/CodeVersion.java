package com.mongs.play.domain.code.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "code_version")
public class CodeVersion {
    @Id
    @Column(name = "build_version")
    private String buildVersion;
    @Column(nullable = false)
    private String codeIntegrity;
    @Column(nullable = false)
    private Boolean mustUpdateApp;
    @Column(nullable = false)
    private LocalDateTime createdAt;
}