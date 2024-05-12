package com.bugbusters.contentservice.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentMetaData {
    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @NotNull
    private String contentName;
    
    @NotNull
    private String contentType;
    
    @NotNull
    private String description;

    // in seconds
    @NotNull
    private Long duration;

    @NotNull
    private String title;

    @NotNull
    private Long size;

    @NotNull
    private LocalDateTime createdAt;
}
