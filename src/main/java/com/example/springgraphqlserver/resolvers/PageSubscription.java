package com.example.springgraphqlserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.example.springgraphqlserver.services.PagePublisher;
import com.example.springgraphqlserver.services.ProductPublisher;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageSubscription implements GraphQLSubscriptionResolver {

    @Autowired
    private PagePublisher pagePublisher;

    public Publisher<String> getScrapesPerformed() {
        return pagePublisher.getScrapesPublisher();
    }
}
