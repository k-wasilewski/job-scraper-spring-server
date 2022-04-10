package com.example.springgraphqlserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.springgraphqlserver.services.PageService;
import com.example.springgraphqlserver.services.ProductService;
import com.example.springgraphqlserver.types.Page;
import com.example.springgraphqlserver.types.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageQuery implements GraphQLQueryResolver {
    @Autowired
    PageService pageService;

    public List<Page> getPages() {
        return pageService.getPages();
    }
}
