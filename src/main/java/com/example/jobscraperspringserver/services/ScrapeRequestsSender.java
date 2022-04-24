package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.utils.HostUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Service
public class ScrapeRequestsSender {

    @Autowired
    PageService pageService;

    private final String NODE_SERVER_ENDPOINT = HostUtils.pingHost("localhost", 8080, 3000) ? "http://localhost:8080/graphql" : "http://job-scraper-node-server:8080/graphql";

    public Date performScrapeRequest(String host, String path, String jobAnchorSelector, String jobLinkContains, int numberOfPages) {
        try {
            URL url = new URL(this.NODE_SERVER_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "*");
            con.setDoOutput(true);

            String _host = host.replaceAll("\"", "&quot");
            String _path = path.replaceAll("\"", "&quot");
            String _jobAnchorSelector = jobAnchorSelector.replaceAll("\"", "&quot");
            String _jobLinkContains = jobLinkContains.replaceAll("\"", "&quot");

            String jsonInputString = "{ \"query\": \"mutation { scrape(host: \\\"" + _host + "\\\", path: \\\"" + _path + "\\\", jobAnchorSelector: \\\"" + _jobAnchorSelector + "\\\", jobLinkContains: \\\"" + _jobLinkContains + "\\\", numberOfPages: " + numberOfPages + ") { complete } }\" }";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (con.getResponseCode() == 200) return new Date();
        } catch (Exception e) {
            System.out.println("performScrapeRequest error: " + e);
        }

        return null;
    }
}
