package com.kachalova.ruleset.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rule_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleSetEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "ruleSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnonymizationRuleEntity> rules;
}
