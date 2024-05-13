package com.bugbusters.course.kafka_models;

import java.time.Duration;
import java.util.UUID;

import com.bugbusters.course.enums.ContentType;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class AddContentRequest {
    private UUID sectionId;
    private Integer orderNumber;
    private Long duration;
    private String title;
    private UUID multimediaId;
    private ContentType type;
}
