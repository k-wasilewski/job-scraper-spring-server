package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import com.example.jobscraperspringserver.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PageService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserService userService;

    public List<Page> getPages() {
        String uuid = userService.getCurrentUserUuid();
        Query query = new Query();
        query.addCriteria(Criteria.where("uuid").is(uuid));
        return mongoTemplate.find(query, Page.class);
    }

    public Page deletePage(int id) {
        String uuid = userService.getCurrentUserUuid();
        Query query = new Query();
        query.addCriteria(Criteria.where("uuid").is(uuid));
        Page toDelete = mongoTemplate.find(query, Page.class).stream().filter(p -> p.getId() == id).findFirst().get();
        if (mongoTemplate.remove(toDelete).getDeletedCount() == 1) {
            return toDelete;
        }
        return null;
    }

    public Page modifyPage(int id, Page page) {
        String uuid = userService.getCurrentUserUuid();
        page.setId(id);
        page.setUserUuid(uuid);
        mongoTemplate.save(page);
        return page;
    }

    public Page addPage(Page page) {
        String uuid = userService.getCurrentUserUuid();
        page.setId(getHighestId() + 1);
        page.setUserUuid(uuid);
        mongoTemplate.insert(page);
        return page;
    }

    private int getHighestId() {
        try {
            return getPages().stream().max(Comparator.comparing(Page::getId)).get().getId();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
