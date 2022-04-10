package com.example.jobscraperspringserver.services;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Component
public class PagePublisher {

    private final FluxProcessor<String, String> processor;
    private final FluxSink<String> sink;

    public PagePublisher() {
        this.processor = DirectProcessor.<String>create().serialize();
        this.sink = processor.sink();
    }

    public Publisher<String> getScrapesPublisher() {
        return processor.map(msg -> {
            System.out.println("Publishing message: " + msg);
            return msg;
        });
    }

    public void publish(String s) {
        sink.next(s);
    }
}
