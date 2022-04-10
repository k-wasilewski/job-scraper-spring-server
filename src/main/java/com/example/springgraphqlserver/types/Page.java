package com.example.springgraphqlserver.types;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("pages")
public class Page {
    @Id
    private int id;
    private String host;
    private String path;
    private String jobAnchorSelector;
    private String jobLinkContains;
    private int numberOfPages;
    private int interval;
    private Date lastScrapePerformed;

    public Page() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJobAnchorSelector() {
        return jobAnchorSelector;
    }

    public void setJobAnchorSelector(String jobAnchorSelector) {
        this.jobAnchorSelector = jobAnchorSelector;
    }

    public String getJobLinkContains() {
        return jobLinkContains;
    }

    public void setJobLinkContains(String jobLinkContains) {
        this.jobLinkContains = jobLinkContains;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String toString() {
        return "Page [" + host + path + "]";
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getLastScrapePerformed() {
        return lastScrapePerformed;
    }

    public void setLastScrapePerformed(Date lastScrapePerformed) {
        this.lastScrapePerformed = lastScrapePerformed;
    }
}
