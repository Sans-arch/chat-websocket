package com.negreira.santiago.chat.controllers;

import com.negreira.santiago.chat.pojos.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final List<Message> messages = new ArrayList<>();

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) {
        messages.add(message);

        printAllMessages();

        return message;
    }

    @MessageMapping("/getPreviousMessages")
    public void sendPreviousMessages() {
        for (Message message : messages) {
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }

    private void printAllMessages() {
        for (Message message : messages) {
            System.out.println(message);
        }

        System.out.println("--------------------------\n");
    }
}
