// package com.bugbusters.contentservice.components;

// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;
// import com.bugbusters.contentservice.commands.TestCommand;
// import com.bugbusters.contentservice.dto.KafkaMessage;
// import com.bugbusters.contentservice.enums.CommandActions;
// import com.bugbusters.contentservice.enums.Commands;
// import com.bugbusters.contentservice.commands.Command;
// import com.bugbusters.contentservice.commands.CreateContentCommand;

// @Component
// @lombok.extern.slf4j.Slf4j
// public class KafKaTopicListeners {

// @KafkaListener(topics = {
// "${spring.kafka.consumer.topic}" }, containerFactory =
// "kafkaListenerFactory", groupId = "${spring.kafka.consumer.group-id}")
// public void consumeCommand(KafkaMessage message) {
// log.info("**** -> Consumed command -> {}", message);
// // Commands commandType = message.command();

// // Command command = null;
// // switch (commandType) {
// // case CREATE_CONTENT_COMMAND:
// // command =
// CreateContentCommand.builder().sectionId(message.sectionId().get())
// //
// .duration(message.duration().get()).orderNumber(message.orderNumber().get())
// // .title(message.title().get()).multimediaId(message.multimediaId().get())
// // .type(message.type().get()).build();
// // break;

// // case TEST_COMMAND:
// // command = new TestCommand();
// // break;

// // default:
// // break;
// // }

// // if (command == null) {
// // return;
// // }

// // CommandActions action = message.action();

// // if (action == CommandActions.UNDO) {
// // command.undo();
// // return;
// // }

// // command.execute();
// }
// }