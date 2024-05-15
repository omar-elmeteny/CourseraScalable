package com.bugbusters.contentservice.dto;

public record ContentGetResponse(String id, String contentName, String contentType, String description, Long duration, String title, Long size, String createdAt) {
    
}
