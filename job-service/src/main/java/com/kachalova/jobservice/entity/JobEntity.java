package com.kachalova.jobservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(name = "input_topic")
    private String inputTopic;

    @Column(name = "output_topic")
    private String outputTopic;

    @ManyToOne(fetch = FetchType.LAZY)  // Связь с RuleSetEntity
    @JoinColumn(name = "rule_set_id", referencedColumnName = "id")  // Внешний ключ на таблицу с набором правил
    private RuleSetEntity ruleSet;  // Ссылка на сущность RuleSetEntity

    private String schedule; // cron expression, может быть null

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
