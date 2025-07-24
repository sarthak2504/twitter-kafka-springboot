package com.bingo.kafka;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TwitterPoll {

    private final TwitterKafkaProducer twitterKafkaProducer;

    public TwitterPoll(TwitterKafkaProducer twitterKafkaProducer) {
        this.twitterKafkaProducer = twitterKafkaProducer;
    }
    @PostConstruct
    public void startPolling() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitter.com/2/tweets/search/recent?query=trending&max_results=10&tweet.fields=created_at,author_id"))
                .header("Authorization","Bearer AAAAAAAAAAAAAAAAAAAAALVm3AEAAAAAKVNRQLh9XfR04JAh20iENJimMOU%3DAtOYnmZvjtY8xZPiET3R39dgp8E7XudsrBWlzhUYhBKp9s3Kzt")
                .GET()
                .build();
        client.sendAsync(request,HttpResponse.BodyHandlers.ofInputStream()).thenAccept(
                response -> {
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))){
                        String line;
                        while ((line = reader.readLine()) != null){
                            twitterKafkaProducer.sendMessage(line);
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        Thread.sleep(100000);
    }
}
