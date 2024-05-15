package com.bugbusters.contentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.bugbusters.contentservice.dto.ContentCreateRequest;
import com.bugbusters.contentservice.dto.ContentCreateResponse;
import com.bugbusters.contentservice.dto.ContentGetResponse;
import com.bugbusters.contentservice.models.ContentMetaData;
import com.bugbusters.contentservice.repos.ContentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

@Slf4j
@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;
    private CouchbaseTemplate template;

    public ContentService(CouchbaseTemplate template, ContentRepository contentRepository) {
        this.template = template;
        this.contentRepository = contentRepository;
    }

    @Value("${content.dir}")
    private String contentDir;

    public ContentCreateResponse createContent(ContentCreateRequest req) {
        MultipartFile file = req.file();

        ContentMetaData contentModel = ContentMetaData.builder()
                .title(req.title())
                .description(req.description())
                .size(req.file().getSize())
                .contentName(req.file().getOriginalFilename())
                .contentType(req.file().getContentType())
                .duration(req.duration())
                .createdAt(LocalDateTime.now())
                .build();

        ContentMetaData savedContent = template.save(contentModel);
        saveToFileSystem(file, savedContent.getId());

        ContentCreateResponse contentCreateResponse = new ContentCreateResponse(savedContent.getId());
        return contentCreateResponse;
    }

    public Boolean saveToFileSystem(MultipartFile file, String id) {
        try {
            String fileExtension = file.getOriginalFilename().split("\\.")[1];
            Path path = Paths.get(contentDir + id + "." + fileExtension);
            synchronized (this) {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (Exception e) {
            log.error("Error saving file", e);
        }
        return false;
    }

    public List<ContentGetResponse> getAllContentMetaData() {
        List<ContentMetaData> contentModels = template.findByQuery(ContentMetaData.class).all();
        List<ContentGetResponse> contentResponses = new ArrayList<>();
        for (ContentMetaData contentModel : contentModels) {
            contentResponses.add(mapFromContentModelToContentResponse(contentModel));
        }
        return contentResponses;
    }

    public ContentGetResponse getContentMetaData(String contentId) {
        Optional<ContentMetaData> contentModel = contentRepository.findById(contentId);
        if (contentModel.isEmpty()) {
            return null;
        }
        return mapFromContentModelToContentResponse(contentModel.get());
    }

    public ContentGetResponse mapFromContentModelToContentResponse(ContentMetaData contentModel) {
        return new ContentGetResponse(
                contentModel.getId(),
                contentModel.getContentName(),
                contentModel.getContentType(),
                contentModel.getDescription(),
                contentModel.getDuration(),
                contentModel.getTitle(),
                contentModel.getSize(),
                contentModel.getCreatedAt().toString());
    }

    public ResponseEntity<Resource> downloadContent(String contentId) {
        try {
            Optional<ContentMetaData> content = contentRepository.findById(contentId);
            if (content.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String fileName = content.get().getContentName();
            String fileExtension = fileName.split("\\.")[1];

            Path path = Paths.get(contentDir + contentId + "." + fileExtension);

            if (!Files.exists(path)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            @SuppressWarnings("null")
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String contentType = Files.probeContentType(path);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                    fileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (IOException e) {
            log.error("Error downloading file", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<StreamingResponseBody> streamVideo(String contentId) {
        try {
            Optional<ContentMetaData> content = contentRepository.findById(contentId);
            if (content.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (!content.get().getContentType().startsWith("video")
                    && !content.get().getContentType().startsWith("audio")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String fileName = content.get().getContentName();
            String fileExtension = fileName.split("\\.")[1];

            Path path = Paths.get(contentDir + contentId + "." + fileExtension);

            if (!Files.exists(path)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            @SuppressWarnings("null")
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String contentType = Files.probeContentType(path);

            if (!contentType.startsWith("video")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            InputStream inputStream = resource.getInputStream();
            StreamingResponseBody responseBody = outputStream -> {
                StreamUtils.copy(inputStream, outputStream);
            };
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error streaming video", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
