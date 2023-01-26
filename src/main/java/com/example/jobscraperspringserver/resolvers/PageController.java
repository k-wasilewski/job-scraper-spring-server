package com.example.jobscraperspringserver.resolvers;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;
import org.reactivestreams.Publisher;

import com.example.jobscraperspringserver.services.PagePublisher;
import com.example.jobscraperspringserver.services.PageService;
import com.example.jobscraperspringserver.types.Page;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;
    @Autowired
    private PagePublisher pagePublisher;

    @QueryMapping("getPages")
    public Publisher<Page> getPages() {
        return pageService.getPages();
    }

    @MutationMapping("addPage")
    public Publisher<Page> addPage(@Argument String host, @Argument String path, @Argument String jobAnchorSelector, @Argument String jobLinkContains, @Argument int numberOfPages, @Argument int interval) {
        Page page = new Page();
        page.setHost(host);
        page.setPath(path);
        page.setJobAnchorSelector(jobAnchorSelector);
        page.setJobLinkContains(jobLinkContains);
        page.setNumberOfPages(numberOfPages);
        page.setInterval(interval);
        return pageService.addPage(page);
    }

    @MutationMapping("deletePage")
    public Publisher<Page> deletePage(@Argument int id) {
        return pageService.deletePage(id);
    }

    @MutationMapping("modifyPage")
    public Publisher<Page> modifyPage(@Argument int id, @Argument String host, @Argument String path, @Argument String jobAnchorSelector, @Argument String jobLinkContains, @Argument int numberOfPages, @Argument int interval) {
        Page page = new Page();
        page.setHost(host);
        page.setPath(path);
        page.setJobAnchorSelector(jobAnchorSelector);
        page.setJobLinkContains(jobLinkContains);
        page.setNumberOfPages(numberOfPages);
        page.setInterval(interval);
        return pageService.modifyPage(id, page);
    }

    @SubscriptionMapping("scrapesPerformed")
    public Publisher<String> getScrapesPerformed() {
        return pagePublisher.getScrapesPublisher();
    }
}
