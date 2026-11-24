package com.example.queue;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QueueController {
    @PostMapping("/v1/enqueue")
    public List<QueueResponse> enqueue(
            @RequestBody List<QueueEntry> queueEntries
    ) {
        LoggerFactory.getLogger(QueueController.class).info(queueEntries.toString());

        List<QueueResponse> responses = new ArrayList<>();

        for(QueueEntry entry : queueEntries) {
            RestClient client = RestClient
                    .builder()
                    .baseUrl("http://localhost:8080")
                    .build();

            String reply = client
                    .method(HttpMethod.valueOf(entry.method()))
                    .uri(entry.endpoint())
                    .body(entry.body())
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String.class);

            if(reply == null) {
                reply = "";
            }

            responses.add(new QueueResponse(
                    entry.id(),
                    reply
            ));
        }

        return responses;
    }
}
