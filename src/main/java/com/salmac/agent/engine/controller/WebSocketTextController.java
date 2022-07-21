package com.salmac.agent.engine.controller;

import com.salmac.agent.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTextController {

    @Autowired
    SimpMessagingTemplate template;

    //sendMessage handles POST request sent to server.
    // this method usage SimpMessagingTemplate to pass message to “/topic/message” destination.
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) {
        template.convertAndSend("/topic/message", message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //receiveMessage is called whenever message is sent from client to “/app/sendMessage”.
    // Here, “/app” prefix is from WebSocket Configuration. Please make note of annotations; MessageMapping and Payload.
    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload Message message) {
        // receive message from client
    }

    //broadcastMessage method just return payload received from “/send” POST request.
    // Returned value is received by clients register at “/topic/message”
    @SendTo("/topic/message")
    public Message broadcastMessage(@Payload Message message) {
        return message;
    }
}