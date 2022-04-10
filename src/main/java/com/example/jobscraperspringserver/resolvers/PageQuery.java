package com.example.jobscraperspringserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.jobscraperspringserver.services.PageService;
import com.example.jobscraperspringserver.types.Page;
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
