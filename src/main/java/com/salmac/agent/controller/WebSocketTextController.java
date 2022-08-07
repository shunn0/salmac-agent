package com.salmac.agent.controller;

import com.salmac.agent.service.ProcessBuilderExecutor;
import com.salmac.agent.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class WebSocketTextController {

    @Autowired
    ProcessBuilderExecutor executor;
    @MessageMapping("/user-all")
    @SendTo("/topic/user")
    public Message sendToAll(@Payload Message message) {
        System.out.println("message received "+ message.getMessage()+ "\n from :"+message.getName());
        List<String> results = executor.editAndRunCmd(message.getMessage());
        Optional<String> result = results.stream().reduce((res1, res2)-> res1+ "   |   "+res2);
        if(result.isPresent() && !result.get().isEmpty()){
            message.setMessage(message.getMessage() +"   |   "+result.get());
        }
        return message;
    }

    /*
    @Autowired
    SimpMessagingTemplate template;

    //sendMessage handles POST request sent to server.
    // this method usage SimpMessagingTemplate to pass message to “/topic/message” destination.
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) {
        System.out.println("request received at post mapping");
        template.convertAndSend("/topic/message", message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //receiveMessage is called whenever message is sent from client to “/app/sendMessage”.
    // Here, “/app” prefix is from WebSocket Configuration. Please make note of annotations; MessageMapping and Payload.
    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload Message message) {
        System.out.println("request received at sendMessage message mapping");
        // receive message from client
    }

    //broadcastMessage method just return payload received from “/send” POST request.
    // Returned value is received by clients register at “/topic/message”
    @SendTo("/topic/message")
    public Message broadcastMessage(@Payload Message message) {
        System.out.println("Inside broadcastMessage()");
        return message;
    } */
}