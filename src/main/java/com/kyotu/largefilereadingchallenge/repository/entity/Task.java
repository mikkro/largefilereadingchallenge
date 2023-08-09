package com.kyotu.largefilereadingchallenge.repository.entity;

import com.kyotu.largefilereadingchallenge.repository.TaskStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @ManyToOne
    @JoinColumn(name = "file_configuration_id")
    private FileConfiguration fileConfiguration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private Long fileSize;

    private String city;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private Statistics statistics;

}
