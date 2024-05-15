package com.bugbusters.course.commands;

import com.bugbusters.course.kafka_config.CommandNames;
import com.bugbusters.course.kafka_requests.AddContentRequest;
import com.bugbusters.course.service.CourseContentService;

import com.guctechie.messages.services.Command;
import org.springframework.stereotype.Service;

@Service(CommandNames.ADD_CONTENT_COMMAND)
public class AddContent implements Command<AddContentRequest, Object> {

    private final CourseContentService courseContentService;

    public AddContent(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    @Override
    public Object execute(AddContentRequest addContentRequest) {
        logger.info("Adding content: {}", addContentRequest);
        courseContentService.createContent(addContentRequest);
        return null;
    }

    @Override
    public Class<AddContentRequest> getRequestType() {
        return AddContentRequest.class;
    }
}
