package com.bugbusters.contentservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record ContentCreateRequest(String title, String description, Long duration, MultipartFile file) {
}
