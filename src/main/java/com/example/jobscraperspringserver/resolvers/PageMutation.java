package com.example.jobscraperspringserver.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.jobscraperspringserver.services.PageService;
import com.example.jobscraperspringserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageMutation implements GraphQLMutationResolver {
    @Autowired
    PageService pageService;

    public Page addPage(String host, String path, String jobAnchorSelector, String jobLinkContains, int numberOfPages, int interval) {
        Page page = new Page();
        page.setHost(host);
        page.setPath(path);
        page.setJobAnchorSelector(jobAnchorSelector);
        page.setJobLinkContains(jobLinkContains);
        page.setNumberOfPages(numberOfPages);
        page.setInterval(interval);
        return pageService.addPage(page); }

    public Page deletePage(int id) {
        return pageService.deletePage(id);
    }

    public Page modifyPage(int id, String host, String path, String jobAnchorSelector, String jobLinkContains, int numberOfPages, int interval) {
        Page page = new Page();
        page.setHost(host);
        page.setPath(path);
        page.setJobAnchorSelector(jobAnchorSelector);
        page.setJobLinkContains(jobLinkContains);
        page.setNumberOfPages(numberOfPages);
        page.setInterval(interval);
        return pageService.modifyPage(id, page);
    }
}
