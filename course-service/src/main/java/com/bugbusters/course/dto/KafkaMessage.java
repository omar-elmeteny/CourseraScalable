package com.bugbusters.course.dto;

import java.time.Duration;
import java.util.Optional;
import com.bugbusters.course.enums.ContentType;
import com.bugbusters.course.enums.CommandActions;
import com.bugbusters.course.enums.Commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaMessage {
    Commands command;
    CommandActions action;
    Long sectionId;
    Duration duration;
    Integer orderNumber;
    String title;
    String multimediaId;
    ContentType type;
    String test;

    public KafkaMessage(Commands command, CommandActions action, Long sectionId, Duration duration, Integer orderNumber,
            String title, String multimediaId, ContentType type, String test) {
        this.command = command;
        this.action = action;
        this.sectionId = sectionId;
        this.duration = duration;
        this.orderNumber = orderNumber;
        this.title = title;
        this.multimediaId = multimediaId;
        this.type = type;
        this.test = test;
    }

    public KafkaMessage() {
    }
}