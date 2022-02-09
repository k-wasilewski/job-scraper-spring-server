package com.example.springgraphqlserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.springgraphqlserver.types.Message;
import org.springframework.stereotype.Component;

@Component
public class HelloQuery implements GraphQLQueryResolver {
    public Message getHello() {
        return new Message().withContent("siemanko from Spring server");
    }
}
