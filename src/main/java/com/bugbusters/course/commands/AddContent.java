package com.bugbusters.course.commands;

import com.bugbusters.course.messages.CommandNames;
import com.bugbusters.course.kafka_models.AddContentRequest;
import com.bugbusters.course.messages.services.Command;
import com.bugbusters.course.service.CourseContentService;

import org.springframework.stereotype.Service;

@Service(CommandNames.ADD_CONTENT_COMMAND)
public class AddContent implements Command<AddContentRequest> {

    private CourseContentService courseContentService;

    public AddContent(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    @Override
    public void execute(AddContentRequest addContentRequest) {
        logger.info("Adding content: {}", addContentRequest);
        courseContentService.createContent(addContentRequest);
    }

    @Override
    public Class<AddContentRequest> getRequestType() {
        return AddContentRequest.class;
    }
}
