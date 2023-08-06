package com.kyotu.largefilereadingchallenge.repository.entity;

import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uri;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Nullable
    private String message;

    private boolean isChanged;

}
