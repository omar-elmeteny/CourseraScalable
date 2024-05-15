package com.bugbusters.course.kafka_requests;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class DeleteSectionContentRequest {
    private ArrayList<UUID> contentIds;
}
