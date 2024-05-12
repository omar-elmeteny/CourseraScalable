package com.bugbusters.contentservice.repos;


import org.springframework.data.repository.CrudRepository;

import com.bugbusters.contentservice.models.ContentMetaData;
import java.util.List;
import java.util.Optional;

public interface ContentRepository extends CrudRepository<ContentMetaData, String> {

    List<ContentMetaData> findByContentName(String contentName);
    List<ContentMetaData> findByContentType(String contentType);
    List<ContentMetaData> findAll();
    Optional<ContentMetaData> findById(String id);
    void deleteById(String id);
}
