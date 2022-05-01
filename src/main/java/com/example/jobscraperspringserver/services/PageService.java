package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PageService {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Page> getPages() {
        return mongoTemplate.findAll(Page.class);
    }

    public Page deletePage(int id) {
        Page toDelete = mongoTemplate.findAll(Page.class).stream().filter(p -> p.getId() == id).findFirst().get();
        if (mongoTemplate.remove(toDelete).getDeletedCount() == 1) {
            return toDelete;
        }
        return null;
    }

    public Page modifyPage(int id, Page page) {
        page.setId(id);
        mongoTemplate.save(page);
        return page;
    }

    public Page addPage(Page page) {
        page.setId(getHighestId() + 1);
        mongoTemplate.insert(page);
        return page;
    }

    private int getHighestId() {
        return getPages().stream().max(Comparator.comparing(Page::getId)).get().getId();
    }
}
