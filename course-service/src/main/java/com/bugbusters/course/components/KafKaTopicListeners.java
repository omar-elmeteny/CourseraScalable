// package com.bugbusters.course.components;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

// import com.bugbusters.course.commands.Command;
// import com.bugbusters.course.commands.CreateContentCommand;
// import com.bugbusters.course.commands.TestCommand;
// import com.bugbusters.course.dto.KafkaMessage;
// import com.bugbusters.course.enums.CommandActions;
// import com.bugbusters.course.enums.Commands;

// @Component
// @lombok.extern.slf4j.Slf4j
// public class KafKaTopicListeners {

// @KafkaListener(topics = {
// "${spring.kafka.consumer.topic}" }, containerFactory =
// "kafkaListenerJsonFactory", groupId = "${spring.kafka.consumer.group-id}")
// public void consumeCommand(KafkaMessage message) {
// log.info("**** -> Consumed command -> {}", message);
// Commands commandType = message.command();

// Command command = null;
// switch (commandType) {
// case CREATE_CONTENT_COMMAND:
// command = CreateContentCommand.builder().sectionId(message.sectionId().get())
// .duration(message.duration().get()).orderNumber(message.orderNumber().get())
// .title(message.title().get()).multimediaId(message.multimediaId().get())
// .type(message.type().get()).build();
// break;

// case TEST_COMMAND:
// command = new TestCommand();
// break;

// default:
// break;
// }

// if (command == null) {
// return;
// }

// CommandActions action = message.action();

// if (action == CommandActions.UNDO) {
// command.undo();
// return;
// }

// command.execute();
// }

// }