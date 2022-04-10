package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PageService {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Page> getPages() {
        List<Page> mockPages = new ArrayList<>();
        Page p1 = new Page();
        p1.setHost("https://myhost.com");
        p1.setPath("/jobs/reactnspring");
        p1.setJobLinkContains("");
        p1.setJobAnchorSelector("a");
        p1.setNumberOfPages(1);
        p1.setId(1);
        p1.setInterval(2000);
        mockPages.add(p1);
        Page p2 = new Page();
        p2.setHost("https://otherhost.pl");
        p2.setPath("/get_jobs/react/spring/get");
        p2.setJobLinkContains("");
        p2.setJobAnchorSelector("li[class*=\"job\"]");
        p2.setNumberOfPages(1);
        p2.setId(2);
        p2.setInterval(1000);
        mockPages.add(p2);
        return mockPages;
        //return mongoTemplate.findAll(Page.class);
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
