package com.bugbusters.contentservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.bugbusters.contentservice.dto.ContentCreateRequest;
import com.bugbusters.contentservice.dto.ContentCreateResponse;
import com.bugbusters.contentservice.dto.ContentGetResponse;
import com.bugbusters.contentservice.dto.KafkaMessage;
import com.bugbusters.contentservice.enums.CommandActions;
import com.bugbusters.contentservice.enums.Commands;
import com.bugbusters.contentservice.enums.ContentType;
import com.bugbusters.contentservice.service.ContentService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ContentCreateResponse createContent(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("duration") Long duration,
            @RequestParam("file") MultipartFile file) {
        log.info("Saving content");
        ContentCreateRequest contentReq = new ContentCreateRequest(title,
                description, duration, file);
        ContentCreateResponse response = contentService.createContent(contentReq);
        return response;
    }

    @GetMapping("")
    public List<ContentGetResponse> getAllContentMetaData() {
        log.info("Getting all content metadata");
        return contentService.getAllContentMetaData();
    }

    @GetMapping("/{contentId}")
    public ContentGetResponse getContentMetaData(@PathVariable String contentId) {
        log.info("Getting content metadata for id: {}", contentId);
        return contentService.getContentMetaData(contentId);
    }

    @GetMapping("/download/{contentId}")
    public ResponseEntity<Resource> downloadContent(@PathVariable String contentId) {
        return contentService.downloadContent(contentId);
    }

    @GetMapping("/stream/{contentId}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable String contentId) {
        return contentService.streamVideo(contentId);
    }

}
