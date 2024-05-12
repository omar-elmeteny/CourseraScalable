package com.bugbusters.contentservice.dto;

import java.time.Duration;
import com.bugbusters.contentservice.enums.CommandActions;
import com.bugbusters.contentservice.enums.Commands;
import com.bugbusters.contentservice.enums.ContentType;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

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