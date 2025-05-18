package com.kachalova.ruleset.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "anonymization_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymizationRuleEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnonymizationMethod method;

    @Column(columnDefinition = "TEXT")
    private String parameters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_set_id", nullable = false)
    private RuleSetEntity ruleSet;
}
