package com.example.jobscraperspringserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.example.jobscraperspringserver.services.ProductPublisher;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductSubscription implements GraphQLSubscriptionResolver {

    @Autowired
    private ProductPublisher productPublisher;

    public Publisher<String> getProductChanges() {
        return productPublisher.getChangesPublisher();
    }
}
