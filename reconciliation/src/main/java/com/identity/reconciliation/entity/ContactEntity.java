package com.identity.reconciliation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String phoneNumber;

    private String email;

    // refers to primary contact id
    private Integer linkedId;

    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum LinkPrecedence {
        PRIMARY,
        SECONDARY
    }
}